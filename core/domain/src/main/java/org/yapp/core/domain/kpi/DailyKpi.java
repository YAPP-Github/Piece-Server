package org.yapp.core.domain.kpi;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;
import org.yapp.core.domain.kpi.vo.MatchingKpi;
import org.yapp.core.domain.kpi.vo.UserActivityKpi;
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;

@Entity
@Getter
@NoArgsConstructor
public class DailyKpi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MatchingKpi matchingKpi;

    @Embedded
    private UserGrowthKpi userGrowthKpi;

    @Embedded
    private UserActivityKpi userActivityKpi;

    @Column(nullable = false, unique = true)
    private LocalDate targetDate;

    public DailyKpi(MatchingKpi matchingKpi, UserGrowthKpi userGrowthKpi,
        UserActivityKpi userActivityKpi, LocalDate targetDate) {
        this.matchingKpi = matchingKpi;
        this.userGrowthKpi = userGrowthKpi;
        this.userActivityKpi = userActivityKpi;
        this.targetDate = targetDate;
    }
}