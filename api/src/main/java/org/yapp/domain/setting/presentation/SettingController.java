package org.yapp.domain.setting.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.setting.application.BlockContactSyncTimeService;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.setting.dto.request.SettingToggleRequest;
import org.yapp.domain.setting.dto.response.BlockContactSyncTimeResponse;
import org.yapp.domain.setting.dto.response.SettingInfoResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingController {

  private final SettingService settingService;
  private final BlockContactSyncTimeService blockContactSyncTimeService;

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

  @GetMapping("/blocks/contacts/sync-time")
  @Operation(summary = "연락처 차단 동기화 시간 조회", description = "연락처 차단 마지막 동기화 시간을 조회합니다.", tags = {
      "설정"})
  @ApiResponse(responseCode = "200", description = "연락처 차단 동기화 시간이 성공적으로 조회되었습니다.")
  public ResponseEntity<CommonResponse<BlockContactSyncTimeResponse>> getBlockContactSyncTime(
      @AuthenticationPrincipal Long userId
  ) {
    BlockContactSyncTimeResponse blockContactSyncTimeResponse = blockContactSyncTimeService.getBlockContactSyncTimeResponse(
        userId);
    return ResponseEntity.ok(CommonResponse.createSuccess(blockContactSyncTimeResponse));
  }
}
