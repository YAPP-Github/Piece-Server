package org.yapp.domain.setting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettingInfoResponse {

  private Boolean isNotificationEnabled;
  private Boolean isMatchingNotificationEnabled;
  private Boolean isAcquaintanceBlockEnabled;
}
