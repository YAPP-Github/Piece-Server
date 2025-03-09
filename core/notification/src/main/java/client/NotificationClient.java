package client;

public interface NotificationClient {

  boolean match(String clientName);

  void sendNotification(String token, String title, String message);
}
