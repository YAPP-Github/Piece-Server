package org.yapp.infra.redis.application;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

  private static final String REDIS_LUA_ADD = """
      local exists = redis.call("EXISTS", KEYS[1])
      if exists == 0 then
          redis.call("BF.RESERVE", KEYS[1], 0.01, 2000)
      end
      
      return redis.call("BF.ADD", KEYS[1], ARGV[1])
      """;

  private static final String REDIS_LUA_CHECK = """
      return redis.call("BF.EXISTS", KEYS[1], ARGV[1])
      """;
  private static final String REDIS_LUA_MADD = """
        local exists = redis.call("EXISTS", KEYS[1])
        if exists == 0 then
            redis.call("BF.RESERVE", KEYS[1], 0.01, 2000)
        end
        return redis.call("BF.MADD", KEYS[1], unpack(ARGV))
      """;

  private final StringRedisTemplate redisTemplate;

  // Bloom Filter 값 추가 (BF.ADD)
  private DefaultRedisScript<Long> bloomFilterAddScript;

  // Bloom Filter 값 존재 여부 확인 (BF.EXISTS)
  private DefaultRedisScript<Long> bloomFilterExistsScript;

  // Bloom Filter에 여러 개의 값을 추가 (BF.MADD)
  private DefaultRedisScript<List> bloomFilterMaddScript;

  @PostConstruct
  public void init() {
    this.bloomFilterAddScript = new DefaultRedisScript<>();
    this.bloomFilterAddScript.setScriptText(REDIS_LUA_ADD);
    this.bloomFilterAddScript.setResultType(Long.class);

    this.bloomFilterMaddScript = new DefaultRedisScript<>();
    this.bloomFilterMaddScript.setScriptText(REDIS_LUA_MADD);
    this.bloomFilterMaddScript.setResultType(List.class);

    this.bloomFilterExistsScript = new DefaultRedisScript<>();
    this.bloomFilterExistsScript.setScriptText(REDIS_LUA_CHECK);
    this.bloomFilterExistsScript.setResultType(Long.class);
  }

  public void deleteKey(String key) {
    redisTemplate.delete(key);
  }

  public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void setKeyWithExpiration(String key, String value, long expiredTime) {
    redisTemplate.opsForValue().set(key, value, Duration.ofMillis(expiredTime));
  }

  public void addToBloomFilter(String key, String value) {
    redisTemplate.execute(bloomFilterAddScript, Collections.singletonList(key), value);
  }

  public void mAddToBloomFilter(String key, List<String> values) {
    try {
      redisTemplate.execute(bloomFilterMaddScript, Collections.singletonList(key),
          values.toArray());
    } catch (RedisSystemException e) {
      // 딱히 넣을 value가 없어서 발생하는 예외. 무시가능.
    }
  }

  public boolean existsInBloomFilter(String key, String value) {
    Long result = redisTemplate.execute(bloomFilterExistsScript, Collections.singletonList(key),
        value);
    return result > 0;
  }

  public void pushRightToList(String key, String value) {
    ListOperations<String, String> listOps = redisTemplate.opsForList();
    listOps.rightPush(key, value);
  }

  public void pushRightAllToList(String key, List<String> values) {
    ListOperations<String, String> listOps = redisTemplate.opsForList();
    listOps.rightPushAll(key, values);
  }

  public String popLeftFromList(String key) {
    ListOperations<String, String> listOps = redisTemplate.opsForList();
    return listOps.leftPop(key);
  }
}
