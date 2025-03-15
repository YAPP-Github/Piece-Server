package org.yapp.domain.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.notification.application.NotificationHistoryService;
import org.yapp.domain.notification.dto.request.NotificationRequest;
import org.yapp.domain.notification.dto.response.NotificationHistoryResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationCenterController {

  private final NotificationHistoryService notificationHistoryService;

  @GetMapping
  @Operation(summary = "알림 목록 조회", description = "알림 목록을 조회합니다(cursor 방식)", tags = {"알림"})
  public ResponseEntity<CommonResponse<List<NotificationHistoryResponse>>> getNotifications(
      @ModelAttribute NotificationRequest notificationRequest,
      @AuthenticationPrincipal Long userId
  ) {
    List<NotificationHistoryResponse> notifications = notificationHistoryService.getNotificationHistories(
        userId,
        notificationRequest.getCursor());
    return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.createSuccess(notifications));
  }

  @PutMapping("/{notificationId}/read")
  @Operation(summary = "알림 읽기", description = "알림을 읽음 표시합니다.", tags = {"알림"})
  public ResponseEntity<CommonResponse<Void>> readNotifications(
      @PathVariable(name = "notificationId") Long notificationId,
      @AuthenticationPrincipal Long userId) {
    notificationHistoryService.checkNotificationRead(userId, notificationId);
    return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.createSuccessWithNoContent());
  }
}
