package org.yapp.domain.setting.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.setting.dto.request.SettingToggleRequest;
import org.yapp.domain.setting.dto.response.SettingInfoResponse;
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

  @PutMapping("/notification")
  public ResponseEntity<CommonResponse<Void>> updateMatchNotificationStatus(
      @RequestBody SettingToggleRequest toggleRequest,
      @AuthenticationPrincipal Long userId) {
    settingService.setMatchNotificationStatus(userId, toggleRequest.getToggle());
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }

  @PutMapping("/notification/match")
  public ResponseEntity<CommonResponse<Void>> updateNotificationStatus(
      @RequestBody SettingToggleRequest toggleRequest,
      @AuthenticationPrincipal Long userId) {
    settingService.setNotificationStatus(userId, toggleRequest.getToggle());
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }

  @PutMapping("/block/acquaintance")
  public ResponseEntity<CommonResponse<Void>> updateAcquaintanceBlockStatus(
      @RequestBody SettingToggleRequest toggleRequest,
      @AuthenticationPrincipal Long userId
  ) {
    settingService.setAcquaintanceBlockStatus(userId, toggleRequest.getToggle());
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }


}
