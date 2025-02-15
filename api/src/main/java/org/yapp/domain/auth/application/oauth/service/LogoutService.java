package org.yapp.domain.auth.application.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.token.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class LogoutService {

  private final RefreshTokenService refreshTokenService;

  public void logout(Long userId) {
    refreshTokenService.deleteRefreshToken(userId);
  }
}
