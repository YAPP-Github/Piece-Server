package org.yapp.domain.block.blocker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.Profile;
import org.yapp.domain.block.application.BloomBlockService;
import org.yapp.domain.setting.application.SettingService;

/**
 * BloomFilter를 이용하여 연락처 기반 차단 확인
 */
@Component
@RequiredArgsConstructor
public class AcquaintanceBasedBlocker {

  private final SettingService settingService;
  private final BloomBlockService bloomBlockService;

  public boolean blocked(Profile blockingProfile, Profile blockedProfile) {
    Long blockingUserId = blockingProfile.getUser().getId();
    boolean acquaintanceBlockEnabled = settingService.isAcquaintanceBlockEnabled(blockingUserId);
    if (!acquaintanceBlockEnabled) {
      return false;
    }
    return bloomBlockService.isBlockedByContact(blockingUserId,
        blockedProfile.getUser().getPhoneNumber());
  }
}
