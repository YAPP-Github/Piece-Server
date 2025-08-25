package org.yapp.kpi.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.kpi.DailyKpi;

@Repository
public interface DailyKpiRepository extends JpaRepository<DailyKpi, Long> {

    Optional<DailyKpi> findByTargetDate(LocalDate targetDate);

    List<DailyKpi> findByTargetDateBetween(LocalDate startDate, LocalDate endDate);
}
