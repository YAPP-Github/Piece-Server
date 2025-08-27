package org.yapp.kpi.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.kpi.DailyKpi;
import org.yapp.core.domain.kpi.vo.MatchingKpi;
import org.yapp.core.domain.kpi.vo.UserActivityKpi;
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;

@Component
@RequiredArgsConstructor
public class KpiLoader {

    private final MatchingKpiFetcher matchingKpiFetcher;
    private final UserGrowthKpiFetcher userGrowthKpiFetcher;
    private final UserActivityKpiFetcher userActivityKpiFetcher;

    @Transactional(readOnly = true)
    public DailyKpi loadDailyKpiByTargetDate(LocalDate targetDate) {
        MatchingKpi matchingKpi = matchingKpiFetcher.fetchByTargetDate(targetDate);
        UserGrowthKpi userGrowthKpi = userGrowthKpiFetcher.fetchByTargetDate(targetDate);
        UserActivityKpi userActivityKpi = userActivityKpiFetcher.fetchByTargetDate(targetDate);

        return new DailyKpi(matchingKpi, userGrowthKpi, userActivityKpi, targetDate);
    }
}
