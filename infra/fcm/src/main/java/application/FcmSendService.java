package application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.NotificationErrorCode;

@Service
@RequiredArgsConstructor
public class FcmSendService {

  public void sendNotificationWithToken(String token, String title, String body) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setNotification(Notification.builder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setToken(token)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }

  public void sendNotificationWithTopic(String topic, String title, String body) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setNotification(Notification.builder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setTopic(topic)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }

  public void sendDataWithToken(String token, Map<String, String> data) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setToken(token)
          .putAllData(data)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }

  public void sendDataWithTopic(String topic, Map<String, String> data) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setTopic(topic)
          .putAllData(data)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }

  public void sendNotificationAndDataWithToken(String token, Map<String, String> data, String title,
      String body) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setNotification(Notification.builder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setToken(token)
          .putAllData(data)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }

  public void sendNotificationAndDataWithTopic(String topic, Map<String, String> data, String title,
      String body) {
    try {
      FirebaseMessaging.getInstance().send(Message.builder()
          .setNotification(Notification.builder()
              .setTitle(title)
              .setBody(body)
              .build())
          .setTopic(topic)
          .putAllData(data)
          .build());
    } catch (FirebaseMessagingException e) {
      throw new ApplicationException(NotificationErrorCode.FCM_SEND_ERROR);
    }
  }
}

