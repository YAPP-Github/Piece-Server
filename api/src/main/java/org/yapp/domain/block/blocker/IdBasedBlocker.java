package org.yapp.domain.block.blocker;

public interface IdBasedBlocker {

  boolean blocked(Long blockingUserId, Long blockedUserId);
}
