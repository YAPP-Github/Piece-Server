package org.yapp.kpi.dao;

import java.time.LocalDate;
import org.yapp.core.domain.kpi.vo.MatchingKpi;

public interface MatchingKpiRepository {

    MatchingKpi fetchKpi(LocalDate startDate, LocalDate endDate);

    MatchingKpi fetchKpi(LocalDate targetDate);
}
