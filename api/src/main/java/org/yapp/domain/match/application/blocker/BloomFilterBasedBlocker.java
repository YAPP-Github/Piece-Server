package org.yapp.domain.match.application.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.yapp.domain.block.application.BloomBlockService;

@Primary
@Component
@RequiredArgsConstructor
public class BloomFilterBasedBlocker implements Blocker {

  private final BloomBlockService bloomBlockService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    return bloomBlockService.isBlocked(blockingUserId, blockedUserId);
  }
}
