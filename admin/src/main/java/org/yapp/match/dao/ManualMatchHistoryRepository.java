package org.yapp.match.dao;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yapp.core.domain.match.ManualMatchHistory;

public interface ManualMatchHistoryRepository extends JpaRepository<ManualMatchHistory, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
            FROM ManualMatchHistory m
            WHERE (m.user1Id = :userId OR m.user2Id = :userId)
              AND m.dateTime BETWEEN :start AND :end
        """)
    boolean existsByUserIdAndDateTimeBetween(
        @Param("userId") Long userId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end);

    List<ManualMatchHistory> findByIsMatchedAndDateTimeLessThanEqual(
        boolean isMatched,
        LocalDateTime dateTime);
}