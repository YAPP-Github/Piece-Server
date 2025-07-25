package org.yapp.domain.kpi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.kpi.event.UserActivityDetectedEvent;

@Component
@RequiredArgsConstructor
public class UserActivityDetectedEventListener {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String DAU_KEY_PREFIX = "DAU:";

    @Async
    @EventListener
    public void onUserActivityDetected(UserActivityDetectedEvent event) {
        if (event.getUserId() == null) {
            return;
        }
        String dailyKey = DAU_KEY_PREFIX + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        redisTemplate.opsForValue().setBit(dailyKey, event.getUserId(), true);
        redisTemplate.expire(dailyKey, 7, TimeUnit.DAYS);
    }
}