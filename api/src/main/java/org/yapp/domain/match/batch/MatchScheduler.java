package org.yapp.domain.match.batch;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.match.DailyMatch;
import org.yapp.infra.discord.DiscordMessageFactory;
import org.yapp.infra.discord.DiscordNotificationService;

@Component
@RequiredArgsConstructor
public class MatchScheduler {

  private final DailyMatch matcher;
  private final DiscordNotificationService discordNotificationService;

  /**
   * 10시에 매칭을 위해 9시부터 배치 시작
   */
  @Scheduled(cron = "0 55 21 * * *")
  public void match() {
    matcher.match();
    discordNotificationService.sendNotification(DiscordMessageFactory.createBasicServiceMessage(
        null, "매칭 배치 작업", "매칭 배치 작업을 완료하였습니다"
    ));
  }
}
