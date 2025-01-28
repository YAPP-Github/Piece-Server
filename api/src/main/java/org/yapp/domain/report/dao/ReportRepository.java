package org.yapp.domain.report.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.domain.report.Report;
import org.yapp.domain.user.User;

public interface ReportRepository extends JpaRepository<Report, Long> {

  boolean existsReportByReporterAndReportedUser(User reporter, User reportedUser);
}
