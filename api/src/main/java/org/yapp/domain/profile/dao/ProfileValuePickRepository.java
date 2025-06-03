package org.yapp.domain.profile.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.profile.ProfileValuePick;

@Repository
public interface ProfileValuePickRepository extends JpaRepository<ProfileValuePick, Long> {

  @Query("SELECT p FROM ProfileValuePick p WHERE p.profile.id = :profileId ORDER BY p.valuePick.id ASC")
  List<ProfileValuePick> findByProfileIdOrderByValuePickId(@Param("profileId") Long profileId);

  List<ProfileValuePick> findByProfileId(Long profileId);

  @Query("SELECT COUNT(*)"
      + "FROM ProfileValuePick p1 "
      + "JOIN ProfileValuePick p2 "
      + "ON p1.selectedAnswer = p2.selectedAnswer "
      + "WHERE p1.profile.id = :user1ProfileId "
      + "AND p2.profile.id = :user2ProfileId")
  int countWeight(Long user1ProfileId, Long user2ProfileId);
}
