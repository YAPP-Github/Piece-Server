package org.yapp.global.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GoogleOauthConfig {

  @Value("${app.google.client.android.id}")
  private String androidGoogleClientId;

  @Value("${app.google.client.ios.id}")
  private String iosGoogleClientId;

  @Bean
  public GoogleIdTokenVerifier googleIdTokenVerifier() {
    return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
        JacksonFactory.getDefaultInstance()).setAudience(
        List.of(androidGoogleClientId, iosGoogleClientId)).build();
  }
}
