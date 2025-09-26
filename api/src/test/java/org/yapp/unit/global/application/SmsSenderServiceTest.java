package org.yapp.unit.global.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.yapp.global.application.SmsSenderService;

@ExtendWith(MockitoExtension.class)
class SmsSenderServiceTest {

    private SmsSenderService smsSenderService;

    @Mock
    private DefaultMessageService messageService;

    @Value("${coolsms.apikey}")
    private String apiKey = "mock-api-key";

    @Value("${coolsms.apisecret}")
    private String apiSecret = "mock-api-secret";

    @Value("${coolsms.fromnumber}")
    private String fromNumber = "01012345678";

    @BeforeEach
    void setUp() {
        smsSenderService = new SmsSenderService();
        ReflectionTestUtils.setField(smsSenderService, "apiKey", apiKey);
        ReflectionTestUtils.setField(smsSenderService, "apiSecret", apiSecret);
        ReflectionTestUtils.setField(smsSenderService, "fromNumber", fromNumber);
        ReflectionTestUtils.setField(smsSenderService, "messageService", messageService);
    }

    @Test
    @DisplayName("원하는 SMS 메시지를 원하는 곳으로 전송한다.")
    void shouldSendSmsMessageCorrectly() {
        // Given
        String to = "01098765432";
        String smsMessage = "Test Message";

        // When
        smsSenderService.sendSMS(to, smsMessage);

        // Then
        ArgumentCaptor<SingleMessageSendingRequest> messageCaptor =
            ArgumentCaptor.forClass(SingleMessageSendingRequest.class);
        Mockito.verify(messageService, times(1)).sendOne(messageCaptor.capture());

        Message capturedMessage = messageCaptor.getValue().getMessage();
        assertThat(capturedMessage.getFrom()).isEqualTo(fromNumber);
        assertThat(capturedMessage.getTo()).isEqualTo(to);
        assertThat(capturedMessage.getText()).isEqualTo(smsMessage);
    }
}