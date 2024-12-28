package org.yapp.domain.auth.application.oauth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.error.code.auth.AuthErrorCode;
import org.yapp.error.exception.ApplicationException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleOauthClient implements OauthClient {
  @Value("${app.google.client.id}")
  private String googleClientId;

  @Override
  public String getOAuthProviderUserId(String token) {
    GoogleIdToken googleIdToken;

    GoogleIdTokenVerifier verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance()).setAudience(
            Collections.singletonList(googleClientId)).build();
    try {
      googleIdToken = verifier.verify(token);
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
