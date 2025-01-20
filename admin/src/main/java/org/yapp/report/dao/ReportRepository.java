package org.yapp.report.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.domain.report.Report;
import org.yapp.report.application.dto.ReportedUserWithReasonDto;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query("SELECT new org.yapp.report.application.dto.ReportedUserWithReasonDto(" +
        "r.reportedUser, COUNT(r), " +
        "(SELECT r2.reason FROM Report r2 " +
        " WHERE r2.reportedUser = r.reportedUser " +
        " ORDER BY r2.createdAt DESC LIMIT 1)) " +
        "FROM Report r " +
        "GROUP BY r.reportedUser " +
        "HAVING COUNT(r) >= 1 " +
        "ORDER BY MAX(r.createdAt) DESC")
    Page<ReportedUserWithReasonDto> findReportedUsersWithLatestReason(Pageable pageable);
}
