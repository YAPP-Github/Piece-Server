package org.yapp.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class BanCheckingService {

  private static final String USER_BLACK_LIST_SET = "user_blacklist";

  private final RedisService redisService;
  private final JwtUtil jwtUtil;

  public boolean existsInBlackList(Long userId) {
    return redisService.existsInSet(USER_BLACK_LIST_SET, String.valueOf(userId));
  }

  public void checkBlackListByRefreshToken(String refreshToken) {
    if (existsInBlackList(jwtUtil.getUserId(refreshToken))) {
      throw new ApplicationException(AuthErrorCode.PERMANENTLY_BANNED);
    }
  }
}
