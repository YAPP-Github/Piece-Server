package org.yapp.domain.match.application.blocker;

public interface Blocker {
  boolean blocked(Long user1, Long user2);
}
