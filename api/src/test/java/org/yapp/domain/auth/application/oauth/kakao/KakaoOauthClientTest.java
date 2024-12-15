package org.yapp.domain.auth.application.oauth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoOauthClientTest {
  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private KakaoOauthClient kakaoOauthClient;

  private String accessToken = "testAccessToken";
  private String userInfoUri = "https://kakaoapi.com/userinfo";
  private String expectedUserId = "12345";

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(kakaoOauthClient, "userInfoUri", "https://kakaoapi.com/userinfo");
  }

  @Test
  @DisplayName("잘못된 토큰이 들어오면 로그인에 실패한다.")
  void testGetOAuthProviderUserId_Failure() {
    // Given
    String accessToken = "invalid-access-token";

    when(restTemplate.exchange(eq(userInfoUri), eq(HttpMethod.GET), any(), eq(JsonNode.class))).thenThrow(
        new RuntimeException());

    // When & Then
    assertThrows(RuntimeException.class, () -> kakaoOauthClient.getOAuthProviderUserId(accessToken));
  }

  @Test
  @DisplayName("액세스 토큰으로 카카오 ID를 가져온다")
  void testGetOAuthProviderUserId_Success() throws JsonProcessingException {
    // Given
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    HttpEntity<Object> entity = new HttpEntity<>(headers);

    JsonNode mockResponseBody = new ObjectMapper().readTree("{\"id\": \"" + expectedUserId + "\"}");

    ResponseEntity<JsonNode> responseEntity = ResponseEntity.ok(mockResponseBody);
    when(restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, JsonNode.class)).thenReturn(responseEntity);

    // When
    String userId = kakaoOauthClient.getOAuthProviderUserId(accessToken);

    // Then
    Assertions.assertThat(userId).isEqualTo(expectedUserId);
  }
}