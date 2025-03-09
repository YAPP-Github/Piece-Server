package org.yapp.domain.report.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class UserReportRequest {

  @NotNull
  private Long reportedUserId;
  @NotNull
  @Length(min = 1, max = 100)
  private String reason;
}
