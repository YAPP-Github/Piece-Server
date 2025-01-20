package org.yapp.report.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.report.application.ReportService;
import org.yapp.report.presentation.response.ReportUserResponse;
import org.yapp.util.CommonResponse;
import org.yapp.util.PageResponse;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/admin/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("")
    public ResponseEntity<CommonResponse<PageResponse<ReportUserResponse>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        PageResponse<ReportUserResponse> reportedUsersWithCount = reportService.getReportedUsersWithCount(
            page, size);

        return ResponseEntity.ok(CommonResponse.createSuccess(reportedUsersWithCount));
    }
}
