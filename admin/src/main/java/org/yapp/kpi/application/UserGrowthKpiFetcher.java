package org.yapp.kpi.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;
import org.yapp.kpi.dao.UserGrowthKpiRepository;

@Component
@RequiredArgsConstructor
public class UserGrowthKpiFetcher {

    private final UserGrowthKpiRepository userGrowthKpiRepository;

    @Transactional(readOnly = true)
    public UserGrowthKpi fetchByTargetDate(LocalDate targetDate) {
        return userGrowthKpiRepository.fetchKpi(targetDate);
    }
}
