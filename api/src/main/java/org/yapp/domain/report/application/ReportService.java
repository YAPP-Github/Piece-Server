package org.yapp.domain.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.domain.report.Report;
import org.yapp.domain.report.dao.ReportRepository;
import org.yapp.domain.report.dto.request.UserReportRequest;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.error.dto.ReportErrorCode;
import org.yapp.error.exception.ApplicationException;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserService userService;

  /**
   * 유저 신고 기능
   *
   * @param reporterId        신고하는 유저의 아이디
   * @param userReportRequest 신고 요청
   * @return 신고 객체
   */
  public Report reportUser(Long reporterId, UserReportRequest userReportRequest) {
    User reporter = userService.getUserById(reporterId);
    User reportedUser = userService.getUserById(userReportRequest.getReportedUserId());
    if (isUserReportedAlready(reporter, reportedUser)) {
      throw new ApplicationException(ReportErrorCode.ALREADY_REPORTED);
    }
    Report report = Report.builder().reporter(reporter).reportedUser(reportedUser)
        .reason(userReportRequest.getReason()).build();
    return reportRepository.save(report);
  }

  private boolean isUserReportedAlready(User reporter, User reportedUser) {
    return reportRepository.existsReportByReporterAndReportedUser(reporter, reportedUser);
  }
}
