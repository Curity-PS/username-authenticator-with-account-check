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

package io.curity.identityserver.plugin.restrictedusername.config;

import se.curity.identityserver.sdk.config.Configuration;
import se.curity.identityserver.sdk.config.annotation.DefaultBoolean;
import se.curity.identityserver.sdk.config.annotation.Description;
import se.curity.identityserver.sdk.service.AccountManager;
import se.curity.identityserver.sdk.service.ExceptionFactory;
import se.curity.identityserver.sdk.service.OriginalQueryExtractor;
import se.curity.identityserver.sdk.service.UserPreferenceManager;

import java.util.Optional;

public interface RestrictedUsernameConfiguration extends Configuration
{

    UserPreferenceManager getUserPreferenceManager();

    OriginalQueryExtractor getOriginalQueryExtractor();

    ExceptionFactory getExceptionFactory();

    AccountManager getAccountmanager();

    @DefaultBoolean(false)
    Boolean autoSubmitPreferredUsername();

    Optional<ExtraContextAttributes> getShowLinkToSetContextAttributes();

    interface ExtraContextAttributes
    {
        @Description("Message key present on the authenticator's link")
        String getMessageKey();

        @Description("Name of the attribute added to context attributes")
        String getContextAttributeName();
    }
}
