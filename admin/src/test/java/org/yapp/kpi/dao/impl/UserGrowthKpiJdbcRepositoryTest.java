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
import org.yapp.core.domain.kpi.vo.UserGrowthKpi;
import org.yapp.kpi.dao.UserGrowthKpiRepository;


@JdbcTest
@Import(UserGrowthKpiJdbcRepository.class)
@DisplayName("UserGrowthJdbcRepository 통합 테스트")
class UserGrowthKpiJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UserGrowthKpiRepository userGrowthKpiRepository;

    private static final LocalDate TARGET_DATE = LocalDate.of(2025, 8, 15);

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    @Test
    @DisplayName("특정 기간 동안의 유저 성장 KPI 데이터를 정확하게 조회한다")
    void fetchKpi_succeeds_with_correct_kpi() {
        UserGrowthKpi kpi = userGrowthKpiRepository.fetchKpi(TARGET_DATE, TARGET_DATE);

        assertNotNull(kpi);

        assertThat(kpi.getNewUserCount()).isEqualTo(50);             // 전체 신규 유저
        assertThat(kpi.getRegisterStatusUserCount()).isEqualTo(10);   // 'REGISTER' 상태 유저
        assertThat(kpi.getCreatedProfileUserCount()).isEqualTo(35);   // 'PENDING'(15) + 'USER'(20)
        assertThat(kpi.getApprovedProfileUserCount()).isEqualTo(20);  // 'USER' 20
    }

    @Test
    @DisplayName("데이터가 없는 기간을 조회하면 모든 KPI가 0으로 반환된다")
    void fetchKpi_returns_zero_kpi_for_empty_range() {
        // given
        LocalDate emptyStartDate = LocalDate.of(2000, 1, 1);
        LocalDate emptyEndDate = LocalDate.of(2000, 1, 1);

        // when
        UserGrowthKpi kpi = userGrowthKpiRepository.fetchKpi(emptyStartDate, emptyEndDate);

        // then
        assertNotNull(kpi);
        assertThat(kpi.getNewUserCount()).isZero();
        assertThat(kpi.getRegisterStatusUserCount()).isZero();
        assertThat(kpi.getCreatedProfileUserCount()).isZero();
        assertThat(kpi.getApprovedProfileUserCount()).isZero();
    }

    private void setupTestData() {
        String userSql = "INSERT INTO user_table (role, created_at) VALUES (:role, :createdAt)";

        LocalDateTime targetDateTime = TARGET_DATE.atTime(10, 0);

        IntStream.range(0, 50).forEach(i -> {
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("createdAt", targetDateTime.plusMinutes(i));

            if (i < 10) {       // 10건: 'REGISTER' Role
                params.addValue("role", "REGISTER");
            } else if (i < 25) { // 15건: 'PENDING' Role
                params.addValue("role", "PENDING");
            } else if (i < 45) { // 20건: 'USER' Role
                params.addValue("role", "USER");
            } else {            // 5건: KPI에 집계되지 않는 Role (e.g., ADMIN)
                params.addValue("role", "ADMIN");
            }

            jdbcTemplate.update(userSql, params);
        });

        LocalDateTime beforeStart = TARGET_DATE.atStartOfDay().minusSeconds(1);
        LocalDateTime onEnd = TARGET_DATE.plusDays(1).atStartOfDay();

        jdbcTemplate.update(userSql, new MapSqlParameterSource()
            .addValue("role", "USER")
            .addValue("createdAt", beforeStart));

        jdbcTemplate.update(userSql, new MapSqlParameterSource()
            .addValue("role", "USER")
            .addValue("createdAt", onEnd));
    }

}