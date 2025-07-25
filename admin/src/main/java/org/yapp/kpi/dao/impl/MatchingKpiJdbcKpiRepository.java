package org.yapp.kpi.dao.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.kpi.vo.MatchingKpi;
import org.yapp.kpi.dao.MatchingKpiRepository;

@Repository
@RequiredArgsConstructor
public class MatchingKpiJdbcKpiRepository implements MatchingKpiRepository {

    private final NamedParameterJdbcTemplate jdbc;

    private static final String MATCHING_KPI_QUERY =
        "SELECT " +
            "    COUNT(*) AS createdMatchCount, " +
            "    SUM(CASE WHEN user_1_match_status = 'UNCHECKED' THEN 1 ELSE 0 END + CASE WHEN user_2_match_status = 'UNCHECKED' THEN 1 ELSE 0 END) AS uncheckedMatchUserCount, "
            +
            "    SUM(CASE WHEN user_1_match_status = 'CHECKED' THEN 1 ELSE 0 END + CASE WHEN user_2_match_status = 'CHECKED' THEN 1 ELSE 0 END) AS checkedMatchUserCount, "
            +
            "    SUM(CASE WHEN user_1_match_status = 'ACCEPTED' THEN 1 ELSE 0 END + CASE WHEN user_2_match_status = 'ACCEPTED' THEN 1 ELSE 0 END) AS acceptedMatchUserCount, "
            +
            "    SUM(CASE WHEN user_1_match_status = 'REFUSED' THEN 1 ELSE 0 END + CASE WHEN user_2_match_status = 'REFUSED' THEN 1 ELSE 0 END) AS refusedMatchUserCount, "
            +
            "    SUM(CASE WHEN user_1_match_status = 'BLOCKED' THEN 1 ELSE 0 END + CASE WHEN user_2_match_status = 'BLOCKED' THEN 1 ELSE 0 END) AS blockedMatchUserCount, "
            +
            "    COUNT(CASE WHEN user_1_match_status = 'ACCEPTED' AND user_2_match_status = 'ACCEPTED' THEN 1 END) AS mutuallyAcceptedMatchCount "
            +
            "FROM match_info " +
            "WHERE created_at >= :start AND created_at < :end";

    @Override
    public MatchingKpi fetchKpi(LocalDate start, LocalDate end) {
        MapSqlParameterSource params = createParams(start, end);

        return jdbc.queryForObject(MATCHING_KPI_QUERY, params, (rs, rowNum) ->
            new MatchingKpi(
                rs.getLong("createdMatchCount"),
                rs.getLong("uncheckedMatchUserCount"),
                rs.getLong("checkedMatchUserCount"),
                rs.getLong("acceptedMatchUserCount"),
                rs.getLong("refusedMatchUserCount"),
                rs.getLong("blockedMatchUserCount"),
                rs.getLong("mutuallyAcceptedMatchCount")
            )
        );
    }

    @Override
    public MatchingKpi fetchKpi(LocalDate targetDate) {
        return fetchKpi(targetDate, targetDate);
    }

    private MapSqlParameterSource createParams(LocalDate start, LocalDate end) {
        return new MapSqlParameterSource()
            .addValue("start", start.atStartOfDay())
            .addValue("end", end.plusDays(1).atStartOfDay());
    }
}