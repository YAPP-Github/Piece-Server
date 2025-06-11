package org.yapp.domain.match.dao;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.match.FreeInstantMatchHistory;

public interface FreeInstantMatchHistoryRepository extends
    JpaRepository<FreeInstantMatchHistory, Long> {

    boolean existsByUserIdAndMatchedTimeBetween(Long userId, LocalDateTime start,
        LocalDateTime end);
}
