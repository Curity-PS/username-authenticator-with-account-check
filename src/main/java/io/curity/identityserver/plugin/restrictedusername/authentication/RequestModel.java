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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import se.curity.identityserver.sdk.Nullable;
import se.curity.identityserver.sdk.service.OriginalQueryExtractor;
import se.curity.identityserver.sdk.web.Request;

public final class RequestModel
{

    @Valid
    @Nullable
    private final Post _postRequestModel;
    private final Get _getRequestModel;

    public RequestModel(Request request, OriginalQueryExtractor originalQueryExtractor)
    {
        _postRequestModel = request.isPostRequest() ? new Post(request) : null;
        _getRequestModel = request.isGetRequest() ? new Get(request, originalQueryExtractor) : null;
    }

    public Post getPostRequestModel()
    {
        if (_postRequestModel == null)
        {
            throw new RuntimeException("POST RequestModel does not exit");
        }
        return _postRequestModel;
    }

    public Get getGetRequestModel()
    {
        if (_getRequestModel == null)
        {
            throw new RuntimeException("GET RequestModel does not exit");
        }
        return _getRequestModel;
    }

    public static final class Post
    {
        @NotBlank(message = "validation.error.username.required")
        private final String username;

        Post(Request request)
        {
            username = request.getFormParameterValueOrError("username");
        }

        public String getUsername()
        {
            return username;
        }
    }

    public static final class Get
    {
        private final @Nullable String _preferredUsername;
        private final boolean _additionalContextAttribute;

        public Get(Request request, OriginalQueryExtractor originalQueryExtractor)
        {
            _preferredUsername = originalQueryExtractor.getAuthorizationRequestQueryParameterValue("login_hint");
            _additionalContextAttribute = request.getQueryParameterNames().contains("additionalContextAttribute");
        }

        public @Nullable String getPreferredUsername()
        {
            return _preferredUsername;
        }

        public boolean additionalContextAttribute()
        {
            return _additionalContextAttribute;
        }
    }
}


