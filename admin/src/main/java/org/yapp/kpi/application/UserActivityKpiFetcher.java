package org.yapp.kpi.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.kpi.vo.UserActivityKpi;
import org.yapp.infra.redis.application.RedisDauService;

@Component
@RequiredArgsConstructor
public class UserActivityKpiFetcher {

    private final RedisDauService redisDauService;

    public UserActivityKpi fetchByTargetDate(LocalDate targetDate) {
        return new UserActivityKpi(redisDauService.getDauCount(targetDate));
    }

}
