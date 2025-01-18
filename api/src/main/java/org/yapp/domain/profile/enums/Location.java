package org.yapp.domain.profile.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
  SEOUL("서울"),
  JEJU("제주"),
  BUSAN("부산"),
  ;
  String name;
}
