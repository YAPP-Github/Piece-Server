package org.yapp.domain.match.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchStatus {
  BEFORE_OPEN("BEFORE OPEN"),
  WAITING("WAITING"),
  RESPONDED("RESPONDED"),
  GREEN_LIGHT("GREEN_LIGHT"),
  MATCHED("MATCHED");

  private final String status;
}
