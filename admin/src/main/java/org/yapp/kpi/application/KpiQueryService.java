package org.yapp.kpi.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.kpi.DailyKpi;
import org.yapp.kpi.dao.DailyKpiRepository;

@Service
@RequiredArgsConstructor
public class KpiQueryService {

    private final DailyKpiRepository dailyKpiRepository;

    @Transactional(readOnly = true)
    public DailyKpi findKpiByTargetDate(LocalDate targetDate) {
        return dailyKpiRepository.findByTargetDate(targetDate).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<DailyKpi> findKpiByRange(LocalDate startDate, LocalDate endDate) {
        List<DailyKpi> existingDailyKpis = dailyKpiRepository.findByTargetDateBetween(startDate,
            endDate);

        Map<LocalDate, DailyKpi> kpiMap = existingDailyKpis.stream()
            .collect(Collectors.toMap(DailyKpi::getTargetDate, dailyKpi -> dailyKpi));

        return startDate.datesUntil(endDate.plusDays(1))
            .map(date -> kpiMap.getOrDefault(date, null))
            .collect(Collectors.toList());
    }
}
