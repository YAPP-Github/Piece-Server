package org.yapp.domain.auth.application.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.domain.enums.OauthType;

@Component
@RequiredArgsConstructor
public class GoogleOauthProvider implements OauthProvider {
    private final GoogleOauthClient googleOauthClient;

    @Override
    public OauthType getOauthType() {
        return OauthType.GOOGLE;
    }

    @Override
    public OauthClient getOAuthClient() {
        return googleOauthClient;
    }
}
