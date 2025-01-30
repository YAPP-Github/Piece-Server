package org.yapp.domain.auth.application.oauth.google;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yapp.exception.ApplicationException;
import org.yapp.exception.error.code.AuthErrorCode;

@ExtendWith(MockitoExtension.class)
class GoogleOauthClientTest {

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;
    @Mock
    private GoogleIdToken googleIdToken;
    @Mock
    private GoogleIdToken.Payload payload;
    @InjectMocks
    private GoogleOauthClient googleOauthClient;

    @Test
    @DisplayName("잘못된 토큰이 들어오면 실패한다.")
    void getOAuthProviderUserId_InvalidToken() throws Exception {
        // given
        String token = "invalid_token";

        when(googleIdTokenVerifier.verify(token)).thenReturn(null);

        // when & then
        ApplicationException exception =
            assertThrows(ApplicationException.class,
                () -> googleOauthClient.getOAuthProviderUserId(token));
        assertThat(exception.getErrorCode()).isEqualTo(AuthErrorCode.OAUTH_ERROR);
    }

    @Test
    @DisplayName("올바른 토큰이 들어오면 id token 의 subject 를 반환한다")
    void getOAuthProviderUserId_Success() throws Exception {
        // given
        String token = "valid_token";
        String expectedUserId = "123456789";

        when(googleIdTokenVerifier.verify(token)).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getSubject()).thenReturn(expectedUserId);

        // when
        String result = googleOauthClient.getOAuthProviderUserId(token);

        // then
        assertThat(result).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("GeneralSecurityException이 발생하면 OAUTH_ERROR을 발생시킨다.")
    void getOAuthProviderUserId_ThrowsGeneralSecurityException() throws Exception {
        // given
        String token = "error_token";

        when(googleIdTokenVerifier.verify(token)).thenThrow(new GeneralSecurityException());

        // when & then
        ApplicationException exception =
            assertThrows(ApplicationException.class,
                () -> googleOauthClient.getOAuthProviderUserId(token));

        assertThat(exception.getErrorCode()).isEqualTo(AuthErrorCode.OAUTH_ERROR);
    }

    @Test
    @DisplayName("IOException이 발생하면 OAUTH_ERROR을 발생시킨다.")
    void getOAuthProviderUserId_ThrowsIOException() throws Exception {
        // given
        String token = "error_token";

        when(googleIdTokenVerifier.verify(token)).thenThrow(new IOException());

        // when & then
        ApplicationException exception =
            assertThrows(ApplicationException.class,
                () -> googleOauthClient.getOAuthProviderUserId(token));

        assertThat(exception.getErrorCode()).isEqualTo(AuthErrorCode.OAUTH_ERROR);
    }
}