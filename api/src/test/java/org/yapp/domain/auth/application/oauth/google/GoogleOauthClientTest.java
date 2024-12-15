package org.yapp.domain.auth.application.oauth.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleOauthClientTest {
  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private GoogleOauthClient googleOauthClient;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(googleOauthClient, "userInfoUri", "google_oauth_uri");
  }

  @Test
  @DisplayName("잘못된 토큰이 들어와서 Oauth 로그인에 실패한다")
  void testGetOAuthProviderUserId_Failure() {
    // Given
    String accessToken = "invalid-access-token";

    when(restTemplate.exchange(eq("google_oauth_uri"), eq(HttpMethod.GET), any(), eq(JsonNode.class))).thenThrow(
        new RuntimeException());

    // When & Then
    assertThrows(RuntimeException.class, () -> googleOauthClient.getOAuthProviderUserId(accessToken));
  }

  @Test
  @DisplayName("액세스 토큰으로 Google Oauth 로그인에 성공한다")
  void testGetOAuthProviderUserId_Success() throws Exception {
    // Given
    String accessToken = "valid-access-token";
    String expectedUserId = "123456789";

    JsonNode mockResponseBody = new ObjectMapper().readTree("{\"sub\": \"" + expectedUserId + "\"}");
    ResponseEntity<JsonNode> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

    when(restTemplate.exchange(eq("google_oauth_uri"), eq(HttpMethod.GET), any(), eq(JsonNode.class))).thenReturn(
        mockResponse);

    // When
    String actualUserId = googleOauthClient.getOAuthProviderUserId(accessToken);

    // Then
    assertThat(actualUserId).isEqualTo(expectedUserId);
  }
}