package org.yapp.profile.dao;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValueTalk;

@Repository
public interface ProfileValueTalkRepository extends JpaRepository<ProfileValueTalk, Long> {

    @Query("SELECT pvt FROM ProfileValueTalk pvt " +
        "JOIN FETCH pvt.valueTalk vt " +
        "WHERE vt.isActive = true AND pvt.profile.id = :profileId")
    List<ProfileValueTalk> findActiveProfileValueTalksByProfileId(
        @Param("profileId") Long profileId);
}