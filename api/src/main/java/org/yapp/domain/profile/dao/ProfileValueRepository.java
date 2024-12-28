package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.ProfileValue;

@Repository
public interface ProfileValueRepository extends JpaRepository<ProfileValue, Long> {
}
