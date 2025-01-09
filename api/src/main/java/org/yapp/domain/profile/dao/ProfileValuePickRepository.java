package org.yapp.domain.profile.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValuePick;

@Repository
public interface ProfileValuePickRepository extends JpaRepository<ProfileValuePick, Long> {

    List<ProfileValuePick> findByProfileId(Long profileId);
}
