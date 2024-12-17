package org.yapp.domain.auth.application.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.domain.enums.OauthType;

@RequiredArgsConstructor
@Component
public class KakaoOauthProvider implements OauthProvider {
    private final KakaoOauthClient kakaoOauthClient;

    @Override
    public OauthType getOauthType() {
        return OauthType.KAKAO;
    }

    @Override
    public OauthClient getOAuthClient() {
        return kakaoOauthClient;
    }
}
