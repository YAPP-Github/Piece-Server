package org.yapp.domain.match.application.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.MatchService;

@Component
@RequiredArgsConstructor
public class PreviousMatchBlocker implements Blocker {

  private final MatchService matchService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    return matchService.wasUsersMatched(blockingUserId, blockedUserId);
  }
}
