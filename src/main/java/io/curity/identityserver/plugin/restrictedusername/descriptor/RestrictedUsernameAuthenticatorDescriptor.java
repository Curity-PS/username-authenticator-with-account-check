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

package io.curity.identityserver.plugin.restrictedusername.descriptor;

import io.curity.identityserver.plugin.restrictedusername.authentication.ResponseRepresentationFunction;
import io.curity.identityserver.plugin.restrictedusername.authentication.RestrictedUsernameRequestHandler;
import io.curity.identityserver.plugin.restrictedusername.config.RestrictedUsernameConfiguration;
import se.curity.identityserver.sdk.Nullable;
import se.curity.identityserver.sdk.authentication.AuthenticatorRequestHandler;
import se.curity.identityserver.sdk.haapi.RepresentationFunction;
import se.curity.identityserver.sdk.plugin.descriptor.AuthenticatorPluginDescriptor;

import java.util.Map;

public class RestrictedUsernameAuthenticatorDescriptor implements AuthenticatorPluginDescriptor<RestrictedUsernameConfiguration>
{
    @Override
    public Map<String, Class<? extends AuthenticatorRequestHandler<?>>> getAuthenticationRequestHandlerTypes()
    {
        return Map.of("index", RestrictedUsernameRequestHandler.class);
    }

    @Override
    public String getPluginImplementationType()
    {
        return "restricted-username";
    }

    @Override
    public Class<? extends RestrictedUsernameConfiguration> getConfigurationType()
    {
        return RestrictedUsernameConfiguration.class;
    }

    @Override
    public @Nullable Map<String, Class<? extends RepresentationFunction>> getRepresentationFunctions()
    {
        return Map.of("authenticate/get", ResponseRepresentationFunction.class);
    }
}
