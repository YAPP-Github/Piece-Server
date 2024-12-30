package org.yapp.domain.auth.application.authorization;

import org.springframework.stereotype.Service;
import org.yapp.global.application.SmsSenderService;

import lombok.RequiredArgsConstructor;

/**
 * SMS 인증 서비스
 */
@Service
@RequiredArgsConstructor
public class SmsAuthService {
  private static final String AUTH_CODE_FORMAT = "[PIECE] 인증 번호는 %d 입니다.";
  private final AuthCodeGenerator authCodeGenerator;
  private final SmsSenderService smsSenderService;

  /**
   * 인증번호 전송
   *
   * @param phoneNumber 인증 번호를 받을 핸드폰 번호
   */
  public void sendAuthCodeTo(String phoneNumber) {
    int authCode = authCodeGenerator.generate();
    String authCodeMessage = String.format(AUTH_CODE_FORMAT, authCode);
    smsSenderService.sendSMS(phoneNumber, authCodeMessage);
  }
}
