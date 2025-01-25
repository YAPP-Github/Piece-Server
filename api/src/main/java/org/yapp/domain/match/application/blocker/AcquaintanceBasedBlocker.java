package org.yapp.domain.match.application.blocker;

import org.springframework.stereotype.Component;
import org.yapp.domain.block.application.BlockService;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcquaintanceBasedBlocker implements Blocker {
  private final BlockService blockService;
  private final UserService userService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    User user = userService.getUserById(blockingUserId);
    return blockService.checkIfUserBlockedPhoneNumber(blockedUserId, user.getPhoneNumber());
  }
}
