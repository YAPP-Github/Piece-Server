package org.yapp.domain.auth.application.authorization;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * 랜덤 인증번호 생성기
 */
@Service
public class AuthCodeGenerator {
  private final SecureRandom secureRandom = new SecureRandom();

  /**
   * 6자리 난수 생성
   *
   * @return 6자리 난수
   */
  public int generate() {
    return secureRandom.nextInt(1000000);
  }
}
