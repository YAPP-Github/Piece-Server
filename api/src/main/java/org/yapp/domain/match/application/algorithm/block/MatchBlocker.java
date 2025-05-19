package org.yapp.domain.match.application.algorithm.block;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.Profile;
import org.yapp.domain.block.blocker.AcquaintanceBasedBlocker;
import org.yapp.domain.block.blocker.IdBasedBlocker;

@Component
@RequiredArgsConstructor
public class MatchBlocker {

  private final IdBasedBlocker idBasedBlocker;
  private final AcquaintanceBasedBlocker acquaintanceBasedBlocker;

  public boolean isBlocked(Profile profile1, Profile profile2) {
    if (idBasedBlocker.blocked(profile1.getUser().getId(), profile2.getUser().getId())) {
      return true;
    }
    if (acquaintanceBasedBlocker.blocked(profile1, profile2)) {
      return true;
    }
    return false;
  }
}
