package org.yapp.kpi.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.kpi.vo.MatchingKpi;
import org.yapp.kpi.dao.MatchingKpiRepository;

@Component
@RequiredArgsConstructor
public class MatchingKpiFetcher {

    private final MatchingKpiRepository matchingKpiRepository;

    @Transactional(readOnly = true)
    public MatchingKpi fetchByTargetDate(LocalDate targetDate) {
        return matchingKpiRepository.fetchKpi(targetDate);
    }
}
