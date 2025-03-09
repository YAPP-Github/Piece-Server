package org.yapp.domain.match.application.blocker;

public interface Blocker {
  boolean blocked(Long blockingUserId, Long blockedUserId);
}
