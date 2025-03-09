package org.yapp.core.notification.client;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.notification.enums.NotificationClientName;
import org.yapp.infra.fcm.application.FcmSendService;

@Service
@RequiredArgsConstructor
public class FcmClient implements NotificationClient {

  private final FcmSendService fcmSendService;

  @Override
  public boolean match(String clientName) {
    return NotificationClientName.FCM_CLIENT.getClientName().equals(clientName);
  }

  @Override
  public void sendNotificationWithData(String token, String title, String body,
      Map<String, String> data) {
    fcmSendService.sendNotificationAndDataWithToken(token, data, title, body);
  }
}
