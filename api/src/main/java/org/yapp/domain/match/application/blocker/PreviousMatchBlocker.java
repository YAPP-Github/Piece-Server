package org.yapp.domain.match.application.blocker;

public class PreviousMatchBlocker implements Blocker {
  @Override
  public boolean blocked(Long blockingUserId, Long blockedUserId) {
    return false;
  }
}
