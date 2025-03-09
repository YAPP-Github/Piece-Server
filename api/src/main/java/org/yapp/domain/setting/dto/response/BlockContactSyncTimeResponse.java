package org.yapp.domain.setting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockContactSyncTimeResponse {

  private LocalDateTime syncTime;
}
