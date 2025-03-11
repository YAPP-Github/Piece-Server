package org.yapp.core.notification.client;

import java.util.Map;

public interface NotificationClient {

  boolean match(String clientName);

  void sendNotificationWithData(String token, String title, String body, Map<String, String> data);
}
