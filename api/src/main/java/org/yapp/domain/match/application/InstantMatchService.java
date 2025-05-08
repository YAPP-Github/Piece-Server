package org.yapp.domain.match.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.MatchErrorCode;
import org.yapp.domain.match.application.blocker.Blocker;
import org.yapp.infra.qdrant.application.QdrantService;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class InstantMatchService {

  private static final String USER_VECTOR_COLLECTION = "user_vector";
  private static final String INSTANT_MATCH_QUEUE_PREFIX = "instant_match:";
  private static final int TOP_K_SIZE = 100;
  private static final int SIMILAR_USER_SIZE_THRESHOLD = 3;

  private final QdrantService qdrantService;
  private final Blocker blocker;
  private final RedisService redisService;

  public Long getInstantMatch(Long userId) {
    String redisKey = INSTANT_MATCH_QUEUE_PREFIX + userId;

    // Redis 큐에서 먼저 가져오기
    String matchedUser = redisService.popLeftFromList(redisKey);
    if (matchedUser != null) {
      return Long.parseLong(matchedUser);
    }

    // Qdrant를 통해 유사 유저 탐색 및 Redis 큐에 저장
    List<Double> userVector = qdrantService.getVectorById(USER_VECTOR_COLLECTION, userId);
    
    List<Long> excludeList = new ArrayList<>(List.of(userId));
    int matchedCount = fillMatchQueue(redisKey, userId, userVector, excludeList);

    if (matchedCount == 0) {
      throw new ApplicationException(MatchErrorCode.NOT_ENOUGH_USER_FOR_INSTANT_MATCH);
    }

    return Long.valueOf(redisService.popLeftFromList(redisKey));
  }

  private int fillMatchQueue(String redisKey, Long userId, List<Double> userVector,
      List<Long> excludeList) {
    int totalMatched = 0;

    while (totalMatched < SIMILAR_USER_SIZE_THRESHOLD) {
      List<Long> similarUsers = qdrantService.searchVectorIdsExcluding(
          USER_VECTOR_COLLECTION, userVector, TOP_K_SIZE, excludeList);

      if (similarUsers.isEmpty()) {
        break;
      }

      excludeList.addAll(similarUsers);

      for (Long similarUserId : similarUsers) {
        if (blocker.blocked(userId, similarUserId)) {
          continue;
        }

        redisService.pushRightToList(redisKey, String.valueOf(similarUserId));
        totalMatched++;
      }
    }

    return totalMatched;
  }
}
