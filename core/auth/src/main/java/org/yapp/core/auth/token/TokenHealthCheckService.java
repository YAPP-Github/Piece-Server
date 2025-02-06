package org.yapp.core.auth.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.SecurityErrorCode;

@Service
@RequiredArgsConstructor
public class TokenHealthCheckService {

  private final JwtUtil jwtUtil;

  public void healthCheck(String token) {
    try {
      jwtUtil.isExpired(token);
    } catch (Exception e) {
      throw new ApplicationException(SecurityErrorCode.EXPIRED_TOKEN);
    }
  }
}
