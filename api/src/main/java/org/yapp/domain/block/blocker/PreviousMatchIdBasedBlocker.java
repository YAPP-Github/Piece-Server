package org.yapp.domain.block.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.MatchService;

@Component
@RequiredArgsConstructor
public class PreviousMatchIdBasedBlocker implements IdBasedBlocker {

  private final MatchService matchService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    return matchService.wasUsersMatched(blockingUserId, blockedUserId);
  }
}
