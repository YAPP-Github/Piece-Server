package org.yapp.domain.setting.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.setting.dto.SettingInfoResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingController {

  private final SettingService settingService;

  @GetMapping("/infos")
  public ResponseEntity<CommonResponse<SettingInfoResponse>> getInfos(
      @AuthenticationPrincipal Long userId) {
    SettingInfoResponse userSettingInfo = settingService.getUserSettingInfo(userId);
    return ResponseEntity.ok(CommonResponse.createSuccess(userSettingInfo));
  }

  @PutMapping("/notification/{switch}")
  public ResponseEntity<CommonResponse<Void>> updateMatchNotificationStatus(
      @PathVariable(name = "switch") boolean switchValue,
      @AuthenticationPrincipal Long userId) {
    settingService.setMatchNotificationStatus(userId, switchValue);
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }

  @PutMapping("/notification/match/{switch}")
  public ResponseEntity<CommonResponse<Void>> updateNotificationStatus(
      @PathVariable(name = "switch") boolean switchValue,
      @AuthenticationPrincipal Long userId) {
    settingService.setNotificationStatus(userId, switchValue);
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }

  @PutMapping("/block/acquaintance/{switch}")
  public ResponseEntity<CommonResponse<Void>> updateAcquaintanceBlockStatus(
      @PathVariable(name = "switch") boolean switchValue,
      @AuthenticationPrincipal Long userId
  ) {
    settingService.setAcquaintanceBlockStatus(userId, switchValue);
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }


}
