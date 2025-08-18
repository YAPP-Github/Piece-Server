package org.yapp.kpi.batch;

import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.kpi.DailyKpi;
import org.yapp.kpi.application.KpiLoader;
import org.yapp.kpi.dao.DailyKpiRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class KpiSaveScheduler {

    private final KpiLoader kpiLoader;
    private final DailyKpiRepository dailyKpiRepository;

    @Scheduled(cron = "0 5 0 * * *")
    public void saveKpi() {
        long startTime = System.currentTimeMillis();
        LocalDate targetDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);

        try {
            log.info("{} 날짜의 KPI 집계를 시작합니다.", targetDate);

            DailyKpi dailyKpi = kpiLoader.loadDailyKpiByTargetDate(targetDate);
            dailyKpiRepository.save(dailyKpi);

            log.info("{} 날짜의 KPI 집계를 성공적으로 완료했습니다.", targetDate);

        } catch (Exception e) {
            log.error("{} 날짜의 KPI 집계 스케줄링 작업 중 에러 발생", targetDate, e);

        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("KPI 집계 작업이 총 {}ms 소요되었습니다.", duration);
        }
    }
}
