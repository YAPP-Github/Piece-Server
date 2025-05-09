package org.yapp.domain.auth.application.authorization;

import java.security.SecureRandom;
import org.springframework.stereotype.Service;

/**
 * 랜덤 인증번호 생성기
 */
@Service
public class AuthCodeGenerator {

  private static final String AUTH_CODE_FORMAT = "%06d";

  private final SecureRandom secureRandom = new SecureRandom();

  /**
   * 6자리 난수 생성
   *
   * @return 6자리 난수
   */
  public String generate() {
    int randomValue = secureRandom.nextInt(1000000);
    return String.format(AUTH_CODE_FORMAT, randomValue);
  }
}
