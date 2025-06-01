package org.yapp.domain.match.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yapp.core.domain.match.MatchInfo;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {

  @Query("SELECT mi FROM MatchInfo mi WHERE (mi.user1.id = :userId or mi.user2.id = :userId) and mi.date = :date")
  Optional<MatchInfo> findDailyMatchByUserIdAndDate(Long userId, LocalDate date);

  @Query("SELECT mi FROM MatchInfo mi WHERE (mi.user1.id = :userId1 and mi.user2.id = :userId2)")
  Optional<MatchInfo> findMatchInfoByIds(Long userId1, Long userId2);

  @Query("SELECT mi FROM MatchInfo mi WHERE (mi.user1.id = :userId or mi.user2.id = :userId)")
  List<MatchInfo> findPreviousMatchesById(Long userId);
}
