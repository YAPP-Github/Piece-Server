package org.yapp.kpi.dao;

import java.time.LocalDate;
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;

public interface UserGrowthKpiRepository {

    UserGrowthKpi fetchKpi(LocalDate start, LocalDate end);

    UserGrowthKpi fetchKpi(LocalDate targetDate);
}
