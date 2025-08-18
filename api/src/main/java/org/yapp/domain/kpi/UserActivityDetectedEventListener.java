package org.yapp.domain.kpi;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.kpi.event.UserActivityDetectedEvent;
import org.yapp.infra.redis.application.RedisDauService;

@Component
@RequiredArgsConstructor
public class UserActivityDetectedEventListener {

    private final RedisDauService redisDauService;
    
    @Async
    @EventListener
    public void onUserActivityDetected(UserActivityDetectedEvent event) {
        if (event.getUserId() == null) {
            return;
        }
        redisDauService.setUserActive(event.getUserId(), LocalDate.now());
    }
}