package org.yapp.domain.auth.application.oauth.kakao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.domain.enums.OauthType;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoOauthProviderTest {
  @Mock
  private KakaoOauthClient kakaoOauthClient;
  @InjectMocks
  private KakaoOauthProvider kakaoOauthProvider;

  @Test
  @DisplayName("카카오 Client 를 반환한다")
  void testGetOAuthClient() {
    OauthClient oauthClient = kakaoOauthProvider.getOAuthClient();
    assertThat(oauthClient).isNotNull();
    assertThat(oauthClient).isEqualTo(kakaoOauthClient);
  }

  @Test
  @DisplayName("getOauthType 메서드가 OauthType.KAKAO를 반환한다")
  void testGetOauthType() {
    assertThat(kakaoOauthProvider.getOauthType()).isEqualTo(OauthType.GOOGLE);
  }
}