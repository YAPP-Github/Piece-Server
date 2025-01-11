package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValueTalk;

@Repository
public interface ProfileValueTalkRepository extends JpaRepository<ProfileValueTalk, Long> {

}
