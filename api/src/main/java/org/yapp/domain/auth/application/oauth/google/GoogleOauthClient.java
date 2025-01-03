package org.yapp.domain.auth.application.oauth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import org.springframework.stereotype.Component;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.error.code.auth.AuthErrorCode;
import org.yapp.error.exception.ApplicationException;

import java.io.IOException;
import java.security.GeneralSecurityException;

import lombok.RequiredArgsConstructor;

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
}
