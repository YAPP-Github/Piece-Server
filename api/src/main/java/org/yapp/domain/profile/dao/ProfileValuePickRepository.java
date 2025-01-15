package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValuePick;

import java.util.List;

@Repository
public interface ProfileValuePickRepository extends JpaRepository<ProfileValuePick, Long> {

  @Query("SELECT p FROM ProfileValuePick p WHERE p.profile.id = :profileId ORDER BY p.valuePick.id ASC")
  List<ProfileValuePick> findByProfileIdOrderByValuePickId(@Param("profileId") Long profileId);
}
