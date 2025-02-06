package org.yapp.domain.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yapp.domain.auth.application.oauth.service.OauthService;
import org.yapp.domain.auth.application.token.RefreshTokenService;
import org.yapp.domain.auth.application.token.TokenHealthCheckService;
import org.yapp.domain.auth.presentation.dto.request.OauthLoginRequest;
import org.yapp.domain.auth.presentation.dto.request.RefreshTokenRequest;
import org.yapp.domain.auth.presentation.dto.request.TokenHealthCheckRequest;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.auth.presentation.dto.response.RefreshedTokensResponse;
import org.yapp.format.CommonResponse;

@Controller
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

  private final OauthService oauthService;
  private final RefreshTokenService refreshTokenService;
  private final TokenHealthCheckService tokenHealthCheckService;

  /**
   * 개발중 소셜 로그인으로 사용자의 accessToken을 가져오기 어렵기 때문에 만든 임시 메서드
   */
  @PostMapping("/test/users/{userId}")
  @Operation(summary = "테스트 로그인", description = "테스트용 임시 토큰을 발급합니다.", tags = {"로그인"})
  public ResponseEntity<CommonResponse<OauthLoginResponse>> getToken(@PathVariable Long userId) {
    OauthLoginResponse response = oauthService.tmpTokenGet(userId);
    return ResponseEntity.ok(CommonResponse.createSuccess(response));
  }

  @PostMapping("/oauth")
  @Operation(summary = "Oauth 로그인", description = "Oauth 로그인을 하고 토큰들을 발급합니다.", tags = {"로그인"})
  public ResponseEntity<CommonResponse<OauthLoginResponse>> oauthLogin(
      @RequestBody OauthLoginRequest request) {
    OauthLoginResponse response = oauthService.login(request);
    return ResponseEntity.ok(CommonResponse.createSuccess(response));
  }

  @PatchMapping("/token/refresh")
  @Operation(summary = "토큰 리프레시", description = "accessToken과 refreshToken을 갱신합니다.", tags = {"로그인"})
  public ResponseEntity<CommonResponse<RefreshedTokensResponse>> refreshToken(
      @RequestBody RefreshTokenRequest request,
      @AuthenticationPrincipal Long userId) {
    RefreshedTokensResponse userRefreshedTokensResponse = refreshTokenService.getUserRefreshTokenResponse(
        userId, request);
    return ResponseEntity.ok(CommonResponse.createSuccess(userRefreshedTokensResponse));
  }

  @GetMapping("/token/health-check")
  @Operation(summary = "토큰 헬스체크", description = "토큰 헬스체크.", tags = {"로그인"})
  public ResponseEntity<CommonResponse<Void>> tokenHealthCheck(
      @RequestBody TokenHealthCheckRequest request) {
    tokenHealthCheckService.healthCheck(request.getToken());
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }
}
