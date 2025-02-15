package org.yapp.domain.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.auth.application.oauth.service.LogoutService;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logout")
public class LogoutController {

  private final LogoutService logoutService;

  @PatchMapping
  public ResponseEntity<CommonResponse<Void>> logout(@AuthenticationPrincipal Long userId) {
    logoutService.logout(userId);
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }
}
