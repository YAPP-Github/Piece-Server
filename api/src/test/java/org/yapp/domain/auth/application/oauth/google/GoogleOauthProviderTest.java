package org.yapp.domain.auth.application.oauth.google;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.domain.enums.OauthType;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GoogleOauthProviderTest {
  @Mock
  private GoogleOauthClient googleOauthClient;

  @InjectMocks
  private GoogleOauthProvider googleOauthProvider;

  @Test
  @DisplayName("Google Oauth Client를 반환한다")
  void testGetOAuthClient() {
    OauthClient oauthClient = googleOauthProvider.getOAuthClient();
    assertThat(oauthClient).isNotNull();
    assertThat(oauthClient).isEqualTo(googleOauthClient);
  }

  @Test
  @DisplayName("getOauthType 메서드가 OauthType.GOOGLE을 반환한다")
  void testGetOauthType() {
    assertThat(googleOauthProvider.getOauthType()).isEqualTo(OauthType.GOOGLE);
  }
}