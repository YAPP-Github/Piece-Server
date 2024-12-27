package org.yapp.domain.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yapp.domain.auth.application.oauth.service.OauthService;
import org.yapp.domain.auth.dto.request.OauthLoginRequest;
import org.yapp.domain.auth.dto.response.OauthLoginResponse;
import org.yapp.util.ApiResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
  private final OauthService oauthService;

  @PostMapping("/oauth")
  public ResponseEntity<ApiResponse<OauthLoginResponse>> oauthLogin(@RequestBody OauthLoginRequest request) {
    OauthLoginResponse response = oauthService.login(request);
    return ResponseEntity.ok(ApiResponse.createSuccess(response));
  }

  /**
   * 개발중 소셜 로그인으로 사용자으 accessToken을 가져오기 어렵기 때문에 만든 임시 메서드
   */
  @PostMapping("/test/users/{userId}")
  public ResponseEntity<ApiResponse<OauthLoginResponse>> getToken(@PathVariable Long userId) {
    OauthLoginResponse response = oauthService.tmpTokenGet(userId);
    return ResponseEntity.ok(ApiResponse.createSuccess(response));
  }
}
