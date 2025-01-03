package org.yapp.domain.auth.application.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthCodeGeneratorTest {

  @Test
  @DisplayName("6자리 난수를 생성한다.")
  void shouldGenerateSixDigitNumber_Success() {
    //given
    AuthCodeGenerator authCodeGenerator = new AuthCodeGenerator();

    //when
    int authCode = authCodeGenerator.generate();

    //then
    assertThat(authCode).isLessThanOrEqualTo(999999).isGreaterThanOrEqualTo(1);
  }
}