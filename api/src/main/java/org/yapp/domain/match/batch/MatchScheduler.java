package org.yapp.domain.match.batch;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.matcher.CoupleMatcher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchScheduler {
  private final CoupleMatcher matcher;
  
  @Scheduled(cron = "0 0 22 * * *")
  public void match() {
    matcher.match();
  }
}
