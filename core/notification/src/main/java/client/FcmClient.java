package client;

import application.FcmSendService;
import enums.NotificationClientName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmClient implements NotificationClient {

  private final FcmSendService fcmSendService;

  @Override
  public boolean match(String clientName) {
    return NotificationClientName.FCM_CLIENT.getClientName().equals(clientName);
  }

  @Override
  public void sendNotification(String token, String title, String message) {
    fcmSendService.sendNotificationWithToken(token, title, message);
  }
}
