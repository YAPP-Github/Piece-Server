package org.yapp.core.notification.provider;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.NotificationErrorCode;
import org.yapp.core.notification.client.NotificationClient;

@Component
@RequiredArgsConstructor
public class NotificationProvider {

  private final List<NotificationClient> notificationClients;

  public NotificationClient getNotificationClient(String clientName) {
    return notificationClients.stream().filter(client -> client.match(clientName)).findFirst()
        .orElseThrow(
            () -> new ApplicationException(NotificationErrorCode.NOTIFICATION_CLIENT_NOT_FOUND));
  }
}
