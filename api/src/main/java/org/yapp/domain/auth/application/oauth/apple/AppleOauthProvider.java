package org.yapp.domain.auth.application.oauth.apple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.domain.enums.OauthType;

@Component
@RequiredArgsConstructor
public class AppleOauthProvider implements OauthProvider {
    private final AppleOauthClient appleOauthClient;

    @Override
    public OauthType getOauthType() {
        return OauthType.APPLE;
    }

    @Override
    public OauthClient getOAuthClient() {
        return this.appleOauthClient;
    }
}