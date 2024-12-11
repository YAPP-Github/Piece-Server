package org.yapp.domain.auth.application.oauth;

public interface OauthClient {
    String getOAuthProviderUserId(String accessToken);
}
