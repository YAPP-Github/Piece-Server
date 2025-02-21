package org.yapp.domain.auth.application.oauth.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.domain.auth.application.oauth.OauthClient;
import org.yapp.domain.auth.application.oauth.apple.dto.AppleAuthResponse;
import org.yapp.global.config.AppleOauthProperties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleOauthClient implements OauthClient {

    private final AppleOauthHelper appleOauthHelper;
    private final AppleOauthProperties appleOauthProperties;
    private final RestTemplate restTemplate;

    public AppleAuthResponse getAppleAuthResponse(
        String clientId,
        String clientSecret,
        String grantType,
        String authorizationCode
    ) {
        String url = "https://appleid.apple.com/auth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", grantType);
        params.add("code", authorizationCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<AppleAuthResponse> response = restTemplate.postForEntity(
                url, entity, AppleAuthResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new ApplicationException(AuthErrorCode.ID_TOKEN_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
        }
    }

    @Override
    public String getOAuthProviderUserId(String authorizationCode) {
        String clientSecret = appleOauthHelper.generateClientSecret();
        AppleAuthResponse appleAuthResponse = this.getAppleAuthResponse(
            appleOauthProperties.getClientId(),
            clientSecret,
            "authorization_code",
            authorizationCode
        );
        String idToken = appleAuthResponse.getIdToken();
        Claims idTokenClaims = appleOauthHelper.decodeIdToken(idToken);
        if (idTokenClaims.containsKey("sub")) {
            return idTokenClaims.get("sub", String.class);
        } else {
            throw new ApplicationException(AuthErrorCode.OAUTH_ID_NOT_FOUND);
        }
    }

    @Override
    public void unlink(String authorizationCode) {
        final String url = "https://appleid.apple.com/auth/revoke";

        String clientSecret = appleOauthHelper.generateClientSecret();
        AppleAuthResponse appleAuthResponse = this.getAppleAuthResponse(
            appleOauthProperties.getClientId(),
            clientSecret,
            "authorization_code",
            authorizationCode);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", appleOauthProperties.getClientId());
        params.add("client_secret", clientSecret);
        params.add("token", appleAuthResponse.getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                url, entity, String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return;
            } else {
                throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
            }
        } catch (Exception e) {
            throw new ApplicationException(AuthErrorCode.OAUTH_ERROR);
        }
    }
}