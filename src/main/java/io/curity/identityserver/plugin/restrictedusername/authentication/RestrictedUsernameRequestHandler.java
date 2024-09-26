/*
 *  Copyright 2024 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.identityserver.plugin.restrictedusername.authentication;

import io.curity.identityserver.plugin.restrictedusername.authentication.RequestModel.Get;
import io.curity.identityserver.plugin.restrictedusername.authentication.RequestModel.Post;
import io.curity.identityserver.plugin.restrictedusername.config.RestrictedUsernameConfiguration;
import se.curity.identityserver.sdk.Nullable;
import se.curity.identityserver.sdk.attribute.*;
import se.curity.identityserver.sdk.authentication.AuthenticationResult;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.errors.ErrorCode;
import se.curity.identityserver.sdk.service.AccountManager;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.OriginalQueryExtractor;
import se.curity.identityserver.sdk.service.UserPreferenceManager;
import se.curity.identityserver.sdk.web.Request;
import se.curity.identityserver.sdk.web.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static se.curity.identityserver.sdk.web.ResponseModel.templateResponseModel;

public class RestrictedUsernameRequestHandler implements AuthenticatorRequestHandler<RequestModel>
{

    private final UserPreferenceManager _userPreferenceManager;
    private final Optional<RestrictedUsernameConfiguration.ExtraContextAttributes> _showLinkToSetContextAttribute;
    private final Boolean _autoPostLoginHint;
    private final AccountManager _accountManager;
    private final ExceptionFactory _exceptionFactory;
    private final OriginalQueryExtractor _originalQueryExtractor;

    public RestrictedUsernameRequestHandler(RestrictedUsernameConfiguration configuration)
    {
        _userPreferenceManager = configuration.getUserPreferenceManager();
        _showLinkToSetContextAttribute = configuration.getShowLinkToSetContextAttributes();
        _autoPostLoginHint = configuration.autoSubmitPreferredUsername();
        _accountManager = configuration.getAccountmanager();
        _exceptionFactory = configuration.getExceptionFactory();
        _originalQueryExtractor = configuration.getOriginalQueryExtractor();
    }

    @Override
    public RequestModel preProcess(Request request, Response response)
    {

        // set the template and model for responses on the NOT_FAILURE scope
        Map<String, Object> data = new HashMap<>(4);
        data.put("username", _userPreferenceManager.getUsername());
        if (_showLinkToSetContextAttribute.isPresent())
        {
            data.put("_showLinkToSetContextAttribute", true);
            data.put("_contextAttributeMessageKey", _showLinkToSetContextAttribute.get().getMessageKey());
        }
        response.setResponseModel(templateResponseModel(data, "authenticate/get"),
                Response.ResponseModelScope.ANY);

        return new RequestModel(request, _originalQueryExtractor);
    }

    @Override
    public Optional<AuthenticationResult> get(RequestModel requestModel, Response response)
    {
        Get getRequestModel = requestModel.getGetRequestModel();

        if (getRequestModel.additionalContextAttribute() && _showLinkToSetContextAttribute.isPresent())
        {
            return Optional.of(createAuthenticationResult(
                    null,
                    _showLinkToSetContextAttribute.get().getContextAttributeName())
            );
        }
        @Nullable String preferredUsername = getRequestModel.getPreferredUsername();
        if (_autoPostLoginHint && preferredUsername != null)
        {
            if (_accountManager.getByUserName(preferredUsername) == null)
            {
                throw _exceptionFactory.badRequestException(ErrorCode.INVALID_INPUT, "error.username.invalid");
            }

            return Optional.of(createAuthenticationResult(getRequestModel.getPreferredUsername(), null));
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthenticationResult> post(RequestModel requestModel, Response response)
    {
        Post postRequestModel = requestModel.getPostRequestModel();

        String username = postRequestModel.getUsername();
        if (_accountManager.getByUserName(username) == null)
        {
            throw _exceptionFactory.badRequestException(ErrorCode.INVALID_INPUT, "error.username.invalid");
        }

        _userPreferenceManager.saveUsername(username);

        return Optional.of(createAuthenticationResult(username, null));
    }

    AuthenticationResult createAuthenticationResult(@Nullable String userName, String contextAttributeName)
    {

        var contextAttributes = ContextAttributes.of(Attributes.of(Attribute.of("iat", new Date().getTime())));
        if (contextAttributeName != null)
        {
            contextAttributes = contextAttributes.with(Attribute.of(contextAttributeName, true));
        }

        String username = userName == null ? "" : userName;
        Attributes subjectAttributes;
        if (userName != null)
        {
            subjectAttributes = Attributes.of("username", username);
        }
        else
        {
            subjectAttributes = Attributes.of();
        }

        return new AuthenticationResult(
                AuthenticationAttributes.of(
                        SubjectAttributes.of(username, subjectAttributes),
                        contextAttributes));
    }
}
