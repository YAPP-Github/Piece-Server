package org.yapp.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.report.application.ReportService;
import org.yapp.domain.report.dto.request.UserReportRequest;
import org.yapp.util.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

  public final ReportService reportService;

  @PostMapping()
  @Operation(summary = "유저 신고", description = "유저를 신고합니다.", tags = {
      "신고"})
  @ApiResponse(responseCode = "200", description = "유저를 성공적으로 신고하였습니다.")
  public ResponseEntity<CommonResponse<Void>> reportUser(@AuthenticationPrincipal Long userId,
      @RequestBody @Valid UserReportRequest request) {
    reportService.reportUser(userId, request);
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }
}
