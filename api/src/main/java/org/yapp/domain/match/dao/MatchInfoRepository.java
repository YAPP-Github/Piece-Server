package org.yapp.domain.match.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yapp.domain.match.MatchInfo;

import java.time.LocalDate;
import java.util.Optional;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {
  @Query("SELECT mi FROM MatchInfo mi WHERE (mi.user1.id = :userId or mi.user2.id = :userId) and mi.date = :date")
  Optional<MatchInfo> findByUserIdAndDate(Long userId, LocalDate date);
}
