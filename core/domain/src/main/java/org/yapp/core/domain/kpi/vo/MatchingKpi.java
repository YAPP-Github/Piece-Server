package org.yapp.core.domain.kpi.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingKpi implements KpiVO {

    private long createdMatchCount;
    private long uncheckedMatchUserCount;
    private long checkedMatchUserCount;
    private long acceptedMatchUserCount;
    private long refusedMatchUserCount;
    private long blockedMatchUserCount;
    private long mutuallyAcceptedMatchCount;

    public double getMutualAcceptanceRate() {
        if (createdMatchCount == 0) {
            return 0;
        }

        return (double) mutuallyAcceptedMatchCount / createdMatchCount;
    }
}