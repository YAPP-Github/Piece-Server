package org.yapp.domain.auth.application.oauth.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yapp.domain.auth.application.oauth.OauthClient;

@Component
@RequiredArgsConstructor
public class KakaoOauthClient implements OauthClient {
    @Value("${oauth.kakaoUserInfoUri}")
    private String userInfoUri;
    private final RestTemplate restTemplate;

    @Override
    public String getOAuthProviderUserId(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody().get("id").asText();
        }
        throw new RuntimeException();
    }
}
