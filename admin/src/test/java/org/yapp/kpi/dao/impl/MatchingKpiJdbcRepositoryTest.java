package org.yapp.kpi.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.yapp.core.domain.kpi.vo.MatchingKpi;

@JdbcTest
@Import(MatchingKpiJdbcRepository.class)
@DisplayName("MatchingKpiJdbcRepository 통합 테스트")
class MatchingKpiJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MatchingKpiJdbcRepository matchingKpiJdbcRepository;

    private static final LocalDate TARGET_START_DATE = LocalDate.of(2025, 7, 20);
    private static final LocalDate TARGET_END_DATE = LocalDate.of(2025, 7, 20);

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    @Test
    @DisplayName("특정 기간 동안의 매칭 KPI 데이터를 정확하게 조회한다")
    void fetchKpi_succeeds_with_correct_kpi() {
        // given - 준비 (Arrange)
        // @BeforeEach 에서 데이터 준비 완료

        // when - 실행 (Act)
        MatchingKpi kpi = matchingKpiJdbcRepository.fetchKpi(TARGET_START_DATE, TARGET_END_DATE);

        // then - 검증 (Assert)
        assertNotNull(kpi);

        assertThat(kpi.getCreatedMatchCount()).isEqualTo(100);
        assertThat(kpi.getUncheckedMatchUserCount()).isEqualTo(40);
        assertThat(kpi.getCheckedMatchUserCount()).isEqualTo(60);
        assertThat(kpi.getAcceptedMatchUserCount()).isEqualTo(
            30L + (15L * 2) + 15);
        assertThat(kpi.getRefusedMatchUserCount()).isEqualTo(10L);
        assertThat(kpi.getBlockedMatchUserCount()).isEqualTo(15L);
        assertThat(kpi.getMutuallyAcceptedMatchCount()).isEqualTo(15);
    }

    @Test
    @DisplayName("데이터가 없는 기간을 조회하면 모든 KPI가 0으로 반환된다")
    void fetchKpi_returns_zero_kpi_for_empty_range() {
        // given
        LocalDate emptyStartDate = LocalDate.of(2000, 1, 1);
        LocalDate emptyEndDate = LocalDate.of(2000, 1, 1);

        // when
        MatchingKpi kpi = matchingKpiJdbcRepository.fetchKpi(emptyStartDate, emptyEndDate);

        // then
        assertNotNull(kpi);
        assertThat(kpi.getCreatedMatchCount()).isZero();
        assertThat(kpi.getUncheckedMatchUserCount()).isZero();
        assertThat(kpi.getCheckedMatchUserCount()).isZero();
        assertThat(kpi.getAcceptedMatchUserCount()).isZero();
        assertThat(kpi.getRefusedMatchUserCount()).isZero();
        assertThat(kpi.getBlockedMatchUserCount()).isZero();
        assertThat(kpi.getMutuallyAcceptedMatchCount()).isZero();
    }


    private void setupTestData() {
        String sql =
            "INSERT INTO match_info (user_1, user_2, user_1_match_status, user_2_match_status, created_at) "
                +
                "VALUES (:user1Id, :user2Id, :status1, :status2, :createdAt)";

        LocalDateTime targetDateTime = TARGET_START_DATE.atTime(10, 0); // 2025-07-20 10:00:00

        // 1. 조회 기간에 포함되는 데이터 100건 생성
        IntStream.range(0, 100).forEach(i -> {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("user1Id", 1000 + i);
            params.addValue("user2Id", 2000 + i);
            params.addValue("createdAt", targetDateTime.plusMinutes(i)); // 시간 분산

            // 쿼리의 모든 CASE 문을 커버하기 위한 분기 처리
            if (i < 10) { // 10건: 양쪽 다 UNCHECKED
                params.addValue("status1", "UNCHECKED");
                params.addValue("status2", "UNCHECKED");
            } else if (i < 30) { // 20건: 한쪽만 CHECKED, UNCHECKED
                params.addValue("status1", "CHECKED");
                params.addValue("status2", "UNCHECKED");
            } else if (i < 60) { // 30건: 한쪽만 ACCEPTED, CHECKED
                params.addValue("status1", "ACCEPTED");
                params.addValue("status2", "CHECKED");
            } else if (i < 70) { // 10건: 한쪽만 REFUSED
                params.addValue("status1", "REFUSED");
                params.addValue("status2", "CHECKED");
            } else if (i < 85) { // 15건: 양쪽 다 ACCEPTED (상호 수락)
                params.addValue("status1", "ACCEPTED");
                params.addValue("status2", "ACCEPTED");
            } else { // 15건: 한쪽 BLOCKED
                params.addValue("status1", "BLOCKED");
                params.addValue("status2", "ACCEPTED");
            }
            jdbcTemplate.update(sql, params);
        });

        jdbcTemplate.update(sql, new MapSqlParameterSource()
            .addValue("user1Id", 9001).addValue("user2Id", 9002)
            .addValue("status1", "ACCEPTED").addValue("status2", "ACCEPTED")
            .addValue("createdAt", TARGET_START_DATE.atStartOfDay().minusSeconds(1))); // 기간 시작 바로 전

        jdbcTemplate.update(sql, new MapSqlParameterSource()
            .addValue("user1Id", 9003).addValue("user2Id", 9004)
            .addValue("status1", "ACCEPTED").addValue("status2", "ACCEPTED")
            .addValue("createdAt", TARGET_END_DATE.plusDays(1).atStartOfDay())); // 기간 종료 시점
    }
}