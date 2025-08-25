package org.yapp.match.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.match.application.command.ManualMatchCommandService;

@Component
@RequiredArgsConstructor
public class ManualMatchScheduler {

    private final ManualMatchCommandService manualMatchCommandService;

    @Scheduled(cron = "0 * * * * *")
    public void manualMatchBatch() {
        manualMatchCommandService.executeReservedMatching(LocalDateTime.now());
    }
}
