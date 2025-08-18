package org.yapp.infra.redis.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisDauService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DAU_KEY_PREFIX = "DAU:";
    private static final long DAU_KEY_EXPIRE_DAYS = 30;

    public void setUserActive(Long userId, LocalDate date) {
        if (userId == null || date == null) {
            return;
        }
        String key = getDauKey(date);

        redisTemplate.opsForValue().setBit(key, userId, true);
        redisTemplate.expire(key, DAU_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    public long getDauCount(LocalDate date) {
        if (date == null) {
            return 0L;
        }

        String key = getDauKey(date);

        Long count = redisTemplate.execute((RedisCallback<Long>) connection ->
            connection.stringCommands().bitCount(key.getBytes())
        );

        return (count != null) ? count : 0L;
    }

    private String getDauKey(LocalDate date) {
        return DAU_KEY_PREFIX + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
