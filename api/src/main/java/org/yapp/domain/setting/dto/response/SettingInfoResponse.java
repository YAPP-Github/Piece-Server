package org.yapp.domain.setting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettingInfoResponse {

  private Boolean isNotificationEnabled;
  private Boolean isMatchNotificationEnabled;
  private Boolean isAcquaintanceBlockEnabled;
}
