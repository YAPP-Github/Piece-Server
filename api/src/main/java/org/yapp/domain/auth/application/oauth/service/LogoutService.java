package org.yapp.domain.auth.application.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.token.RefreshTokenService;
import org.yapp.domain.user.application.UserService;

@Service
@RequiredArgsConstructor
public class LogoutService {

  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public void logout(Long userId) {
    userService.deleteFcmToken(userId);
    refreshTokenService.deleteRefreshToken(userId);
  }
}
