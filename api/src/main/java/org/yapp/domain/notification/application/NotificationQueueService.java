package org.yapp.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class NotificationQueueService {

  private static final String MATCH_NOTIFICATION_QUEUE_PREFIX = "alarm:match";

  private final RedisService redisService;

  public void pushUserIdToMatchNotificationQueue(Long userId) {
    redisService.pushRightToList(MATCH_NOTIFICATION_QUEUE_PREFIX, userId.toString());
  }

  public Long popUserIdFromMatchNotificationQueue() {
    String userIdString = redisService.popLeftFromList(MATCH_NOTIFICATION_QUEUE_PREFIX);
    if (userIdString == null) {
      return null;
    }
    return Long.valueOf(userIdString);
  }
}
