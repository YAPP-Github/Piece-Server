package org.yapp.core.domain.kpi.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityKpi implements KpiVO {

    private Long activeUserCount;
}
