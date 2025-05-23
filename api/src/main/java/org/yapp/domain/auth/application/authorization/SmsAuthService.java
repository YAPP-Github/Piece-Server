package org.yapp.domain.auth.application.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.SmsAuthErrorCode;
import org.yapp.domain.auth.presentation.dto.response.SmsAuthResponse;
import org.yapp.global.application.SmsSenderService;
import org.yapp.infra.redis.application.RedisService;

/**
 * SMS 인증 서비스
 */
@Service
@RequiredArgsConstructor
public class SmsAuthService {

    private static final long AUTH_CODE_EXPIRE_TIME = 300000;
    private static final String AUTH_CODE_KEY_PREFIX = "authcode:";
    private static final String AUTH_CODE_FORMAT = "[PIECE] 인증 번호는 %s 입니다.";
    private final AuthCodeGenerator authCodeGenerator;
    private final SmsSenderService smsSenderService;
    private final RedisService redisService;

    @Value("${auth.mock-phone-number}")
    private String mockPhoneNumber;

    @Value("${auth.mock-sms-auth-code}")
    private String mockSmsAuthCode;

    /**
     * 인증번호 전송
     *
     * @param phoneNumber 인증 번호를 받을 핸드폰 번호
     */
    public SmsAuthResponse sendAuthCodeTo(String phoneNumber) {
        String authCode = authCodeGenerator.generate();

        if (mockPhoneNumber.equals(phoneNumber)) {
            authCode = mockSmsAuthCode;
        }

        redisService.setKeyWithExpiration(AUTH_CODE_KEY_PREFIX + phoneNumber, authCode,
            AUTH_CODE_EXPIRE_TIME);
        String authCodeMessage = String.format(AUTH_CODE_FORMAT, authCode);
        smsSenderService.sendSMS(phoneNumber, authCodeMessage);
        return new SmsAuthResponse(phoneNumber);
    }

    /**
     * 인증번호 인증
     *
     * @param phoneNumber 핸드폰번호
     * @param code        인증 번호
     * @return 인증번호 일치 여부
     */
    public void verifySmsAuthCode(String phoneNumber, String code) {
        String expectedCode = redisService.getValue(AUTH_CODE_KEY_PREFIX + phoneNumber);
        if (expectedCode == null) {
            throw new ApplicationException(SmsAuthErrorCode.CODE_NOT_EXIST);
        }
        if (!expectedCode.equals(code)) {
            throw new ApplicationException(SmsAuthErrorCode.CODE_NOT_CORRECT);
        }
    }
}
