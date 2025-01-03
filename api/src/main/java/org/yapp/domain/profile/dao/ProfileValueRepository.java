package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValue;

import java.util.List;

@Repository
public interface ProfileValueRepository extends JpaRepository<ProfileValue, Long> {
    List<ProfileValue> findByProfileId(Long profileId);
}
