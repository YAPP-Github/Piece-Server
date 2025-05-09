package org.yapp.domain.block.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.block.application.DirectBlockService;

@Component
@RequiredArgsConstructor
public class DirectBlockBasedBlocker implements IdBasedBlocker {

  private final DirectBlockService directBlockService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    return directBlockService.checkBlock(blockingUserId, blockedUserId);
  }
}
