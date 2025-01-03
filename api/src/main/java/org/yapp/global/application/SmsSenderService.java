package org.yapp.global.application;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsSenderService {
  @Value("${coolsms.apikey}")
  private String apiKey;
  @Value("${coolsms.apisecret}")
  private String apiSecret;
  @Value("${coolsms.fromnumber}")
  private String fromNumber;
  private DefaultMessageService messageService;

  @PostConstruct // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
  public void init() {
    this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
  }

  public void sendSMS(String to, String smsMessage) {
    Message message = new Message();
    message.setFrom(fromNumber);
    message.setTo(to);
    message.setText(smsMessage);

    this.messageService.sendOne(new SingleMessageSendingRequest(message));
  }
}
