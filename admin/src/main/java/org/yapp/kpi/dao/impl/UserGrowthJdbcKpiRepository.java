package org.yapp.kpi.dao.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;
import org.yapp.kpi.dao.UserGrowthKpiRepository;

@Repository
@RequiredArgsConstructor
public class UserGrowthJdbcKpiRepository implements UserGrowthKpiRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private static final String USER_GROWTH_KPI_QUERY =
        "SELECT "
            + "COUNT(*) AS newUserCount,"
            + "COUNT(CASE WHEN role = 'REGISTER' THEN 1 END) AS registerStatusUserCount,"
            + "COUNT(CASE WHEN role IN ('PENDING', 'USER') THEN 1 END) AS createdProfileUserCount,"
            + "COUNT(CASE WHEN role = 'USER' THEN 1 END) AS approvedProfileUserCount "
            + "FROM user_table "
            + "WHERE created_at >= :start AND created_at < :end;";

    @Override
    public UserGrowthKpi fetchKpi(LocalDate start, LocalDate end) {
        MapSqlParameterSource params = createParams(start, end);

        return jdbc.queryForObject(USER_GROWTH_KPI_QUERY, params, (rs, rowNum) ->
            new UserGrowthKpi(
                rs.getLong("newUserCount"),
                rs.getLong("registerStatusUserCount"),
                rs.getLong("createdProfileUserCount"),
                rs.getLong("approvedProfileUserCount")
            )
        );
    }

    @Override
    public UserGrowthKpi fetchKpi(LocalDate targetDate) {
        return fetchKpi(targetDate, targetDate);
    }

    private MapSqlParameterSource createParams(LocalDate start, LocalDate end) {
        return new MapSqlParameterSource()
            .addValue("start", start.atStartOfDay())
            .addValue("end", end.plusDays(1).atStartOfDay());
    }
}