package org.yapp.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDeleteRequest {

  @Size(max = 100, message = "탈퇴 이유는 100자 이하여야합니다.")
  private String reason;
}
