package org.yapp.domain.auth.application.token;

import org.springframework.stereotype.Service;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.SecurityErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenHealthCheckService {
  private final JwtUtil jwtUtil;

  public void healthCheck(String token) {
    try {
      jwtUtil.isExpired(token);
    } catch (Exception e) {
      throw new ApplicationException(SecurityErrorCode.EXPIRED_ACCESS_TOKEN);
    }
  }
}
