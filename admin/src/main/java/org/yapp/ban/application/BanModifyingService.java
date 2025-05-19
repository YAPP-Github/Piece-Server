package org.yapp.ban.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class BanModifyingService {

  private static final String USER_BLACK_LIST_SET = "user_blacklist";

  private final RedisService redisService;

  public void addUserToBlackList(Long userId) {
    redisService.addToSet(USER_BLACK_LIST_SET, String.valueOf(userId));
  }
}