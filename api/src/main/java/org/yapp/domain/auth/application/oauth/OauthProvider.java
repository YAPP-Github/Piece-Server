package org.yapp.domain.auth.application.oauth;

import org.yapp.domain.auth.domain.enums.OauthType;

public interface OauthProvider {

    OauthType getOauthType();

    OauthClient getOAuthClient();

    default boolean match(String providerName) {
        return providerName.equals(getOauthType().getTypeName());
    }

    default String getOAuthProviderUserId(String accessToken) {
        OauthClient client = getOAuthClient();
        return client.getOAuthProviderUserId(accessToken);
    }

    default void unlink(String accessToken) {
        OauthClient client = getOAuthClient();
        client.unlink(accessToken);
    }
}
