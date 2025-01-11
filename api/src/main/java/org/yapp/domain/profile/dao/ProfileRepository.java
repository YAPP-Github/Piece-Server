package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.Profile;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
  boolean existsByProfileBasic_Nickname(String nickname);

  List<Profile> findByProfileBasic_Location(String location);
}
