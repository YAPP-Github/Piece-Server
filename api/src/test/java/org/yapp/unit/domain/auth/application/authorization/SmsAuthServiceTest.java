package org.yapp.unit.domain.auth.application.authorization;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.yapp.domain.auth.application.authorization.AuthCodeGenerator;
import org.yapp.domain.auth.application.authorization.SmsAuthService;
import org.yapp.global.application.SmsSenderService;
import org.yapp.infra.redis.application.RedisService;

@ExtendWith(MockitoExtension.class)
class SmsAuthServiceTest {

    private SmsAuthService smsAuthService;

    @Mock
    private AuthCodeGenerator authCodeGenerator;

    @Mock
    private SmsSenderService smsSenderService;

    @Mock
    private RedisService redisService;

    private final String mockPhoneNumber = "01012345678";
    private final String mockSmsAuthCode = "123456";

    @BeforeEach
    void setUp() {
        smsAuthService = new SmsAuthService(authCodeGenerator, smsSenderService, redisService);
        ReflectionTestUtils.setField(smsAuthService, "mockPhoneNumber", mockPhoneNumber);
        ReflectionTestUtils.setField(smsAuthService, "mockSmsAuthCode", mockSmsAuthCode);
    }

    @Test
    @DisplayName("인증코드를 SMS 서비스로 전송한다 - General")
    void shouldSendAuthCodeToPhoneNumber() {
        String testPhoneNumber = "01012129982";
        String testSmsAuthCode = "987654";

        when(authCodeGenerator.generate()).thenReturn(testSmsAuthCode);

        // When
        smsAuthService.sendAuthCodeTo(testPhoneNumber);

        // Then
        verify(smsSenderService, times(1))
            .sendSMS(testPhoneNumber, String.format("[PIECE] 인증 번호는 %s 입니다.", testSmsAuthCode));
    }

    @Test
    @DisplayName("인증코드를 SMS 서비스로 전송한다 - MOCK")
    void shouldSendAuthCodeToPhoneNumberMcck() {
        when(authCodeGenerator.generate()).thenReturn(mockSmsAuthCode);

        // When
        smsAuthService.sendAuthCodeTo(mockPhoneNumber);

        // Then
        verify(smsSenderService, times(1))
            .sendSMS(mockPhoneNumber, String.format("[PIECE] 인증 번호는 %s 입니다.", mockSmsAuthCode));
    }
}