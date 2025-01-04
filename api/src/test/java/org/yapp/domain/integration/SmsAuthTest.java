package org.yapp.domain.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yapp.domain.auth.application.authorization.SmsAuthService;

@SpringBootTest
public class SmsAuthTest {
  @Autowired
  private SmsAuthService smsAuthService;

  @Test
  @DisplayName("실제 sms 전송을 테스트 한다")
  void sendSmsTest() {
    //    smsAuthService.sendAuthCodeTo("01012345678"); // 실제로 전송됨
  }
}
