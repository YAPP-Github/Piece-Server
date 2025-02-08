package client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApnClient implements NotificationClient {

  @Override
  public boolean match(String clientName) {
    return false;
  }

  @Override
  public void sendNotification(String token, String title, String message) {

  }
}
