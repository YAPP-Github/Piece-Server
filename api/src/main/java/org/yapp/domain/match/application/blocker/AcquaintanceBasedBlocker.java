package org.yapp.domain.match.application.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.user.User;
import org.yapp.domain.block.application.BlockContactService;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.user.application.UserService;

@Component
@RequiredArgsConstructor
public class AcquaintanceBasedBlocker implements Blocker {

  private final BlockContactService blockContactService;
  private final UserService userService;
  private final SettingService settingService;

  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    boolean acquaintanceBlockEnabled = settingService.isAcquaintanceBlockEnabled(blockingUserId);
    if (!acquaintanceBlockEnabled) {
      return false;
    }
    User user = userService.getUserById(blockingUserId);
    return blockContactService.checkIfUserBlockedPhoneNumber(blockedUserId,
        user.getPhoneNumber());
  }
}
