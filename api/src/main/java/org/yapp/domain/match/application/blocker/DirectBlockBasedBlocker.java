package org.yapp.domain.match.application.blocker;

public class DirectBlockBasedBlocker implements Blocker {
  @Override
  public boolean blocked(Long user1, Long user2) {
    return false;
  }
}
