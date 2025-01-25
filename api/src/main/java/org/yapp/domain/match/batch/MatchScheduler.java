package org.yapp.domain.match.batch;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.matcher.CoupleMatcher;

@Component
@RequiredArgsConstructor
public class MatchScheduler {

  private final CoupleMatcher matcher;

  /**
   * 10시에 매칭을 위해 9시부터 배치 시작
   */
  @Scheduled(cron = "0 0 21 * * *")
  public void match() {
    matcher.match();
  }
}
