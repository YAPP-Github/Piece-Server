package org.yapp.domain.report.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.domain.report.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
