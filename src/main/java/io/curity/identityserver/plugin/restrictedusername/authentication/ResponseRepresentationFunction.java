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

import se.curity.identityserver.sdk.haapi.*;
import se.curity.identityserver.sdk.http.HttpMethod;
import se.curity.identityserver.sdk.http.MediaType;
import se.curity.identityserver.sdk.web.Representation;

import java.net.URI;

public final class ResponseRepresentationFunction implements RepresentationFunction
{

    Message titleMessage = Message.ofKey("view.authenticate");
    Message submitMessage = Message.ofKey("view.submit");
    Message usernameMessage = Message.ofKey("view.username");

    @Override
    public Representation apply(RepresentationModel model, RepresentationFactory factory)
    {
        URI authUrl = URI.create(model.getString("_authUrl"));
        String username = model.getOptionalString("username").orElse(null);

        return factory.newAuthenticationStep(builder -> builder
                .addFormAction(HaapiContract.Actions.Kinds.LOGIN,
                        authUrl,
                        HttpMethod.POST,
                        MediaType.X_WWW_FORM_URLENCODED,
                        titleMessage,
                        submitMessage,
                        fields -> fields.addUsernameField("username", usernameMessage, username)));
    }
}
