package org.yapp.domain.profile.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValueTalk;

@Repository
public interface ProfileValueTalkRepository extends JpaRepository<ProfileValueTalk, Long> {

    List<ProfileValueTalk> findByProfileId(Long profileId);

}
