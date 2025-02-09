package application;

import client.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import provider.NotificationProvider;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationProvider notificationProvider;

  public void sendNotification(String clientName, String token, String title, String message) {
    NotificationClient notificationClient = notificationProvider.getNotificationClient(
        clientName);
    notificationClient.sendNotification(token, title, message);
  }
}
