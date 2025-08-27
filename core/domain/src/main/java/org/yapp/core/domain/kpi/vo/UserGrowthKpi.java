package org.yapp.core.domain.kpi.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserGrowthKpi implements KpiVO {

    private long newUserCount;
    private long registerStatusUserCount;
    private long createdProfileUserCount;
    private long approvedProfileUserCount;

    public double getProfileCreateRate() {
        if (newUserCount == 0) {
            return 0;
        }

        return (double) createdProfileUserCount / newUserCount;
    }

    public double getProfileApprovedRate() {
        if (createdProfileUserCount == 0) {
            return 0;
        }

        return (double) approvedProfileUserCount / createdProfileUserCount;
    }
}
