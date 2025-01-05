package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValueItem;

import java.util.List;

@Repository
public interface ProfileValueRepository extends JpaRepository<ProfileValueItem, Long> {
    List<ProfileValueItem> findByProfileId(Long profileId);
}
