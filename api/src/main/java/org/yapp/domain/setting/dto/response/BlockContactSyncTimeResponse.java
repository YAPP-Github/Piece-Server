package org.yapp.domain.setting.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.yapp.domain.setting.dto.serializer.BlockContactSyncTimeResponseSerializer;

@Getter
@AllArgsConstructor
public class BlockContactSyncTimeResponse {

  @JsonSerialize(using = BlockContactSyncTimeResponseSerializer.class)
  private LocalDateTime syncTime;
}
