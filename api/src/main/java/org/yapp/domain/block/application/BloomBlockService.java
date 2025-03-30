package org.yapp.domain.block.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.infra.redis.application.RedisService;

@RequiredArgsConstructor
@Service
public class BloomBlockService {

  private static final String BLOOM_FILTER_BLOCK_BY_ID_KEY_PREFIX = "blockById:";
  private static final String BLOOM_FILTER_BLOCK_BY_CONTACT_KEY_PREFIX = "blockByContact:";
  private final RedisService redisService;

  public void blockUserIdList(Long userId, List<Long> blockedUserIdList) {
    List<String> stringUserIdList = blockedUserIdList.stream().map(String::valueOf).toList();
    redisService.mAddToBloomFilter(BLOOM_FILTER_BLOCK_BY_ID_KEY_PREFIX + userId, stringUserIdList);
  }

  public void blockUserId(Long userId, Long blockedUserId) {
    redisService.addToBloomFilter(BLOOM_FILTER_BLOCK_BY_ID_KEY_PREFIX + userId,
        String.valueOf(blockedUserId));
  }

  public void blockContactList(Long userId, List<String> contactList) {
    redisService.mAddToBloomFilter(BLOOM_FILTER_BLOCK_BY_CONTACT_KEY_PREFIX + userId, contactList);
  }

  public void blockContact(Long userId, String contact) {
    redisService.addToBloomFilter(BLOOM_FILTER_BLOCK_BY_CONTACT_KEY_PREFIX + userId, contact);
  }

  public boolean isBlocked(Long userId, Long blockedUserId) {
    return redisService.existsInBloomFilter(BLOOM_FILTER_BLOCK_BY_ID_KEY_PREFIX + userId,
        String.valueOf(blockedUserId));
  }

  public boolean isBlockedByContact(Long userId, String contact) {
    return redisService.existsInBloomFilter(BLOOM_FILTER_BLOCK_BY_CONTACT_KEY_PREFIX + userId,
        contact);
  }

  public void deleteContactBlock(Long userId) {
    redisService.deleteKey(BLOOM_FILTER_BLOCK_BY_CONTACT_KEY_PREFIX + userId);
  }

  public void deleteUserIdBlock(Long userId) {
    redisService.deleteKey(BLOOM_FILTER_BLOCK_BY_ID_KEY_PREFIX + userId);
  }
}
