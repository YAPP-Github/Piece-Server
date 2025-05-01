package org.yapp.core.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleStatus {
  NONE("NONE"),
  REGISTER("REGISTER"),
  PENDING("PENDING"),
  BANNED("BANNED"),
  USER("USER");
  private String status;

  @JsonValue
  public String getStatus() {
    return status;
  }
}
