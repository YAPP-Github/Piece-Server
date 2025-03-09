package org.yapp.domain.auth.application.oauth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.domain.auth.application.oauth.OauthClient;

@Component
@RequiredArgsConstructor
public class GoogleOauthClient implements OauthClient {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public String getOAuthProviderUserId(String token) {
        GoogleIdToken googleIdToken;

        try {
            googleIdToken = googleIdTokenVerifier.verify(token);
        } catch (GeneralSecurityException e) {
            throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
        } catch (IOException e) {
            throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
        }
        if (googleIdToken == null) {
            throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
        }
        return googleIdToken.getPayload().getSubject();
    }

    @Override
    public void unlink(String accessToken) {
        /*
            TODO: 구글 소셜 unlink 로직 필요
        */
        return;
    }
}
