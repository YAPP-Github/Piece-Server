package org.yapp.domain.profile.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.profile.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByProfileBasic_Nickname(String nickname);

    List<Profile> findByProfileBasic_LocationAndUser_Role(String location, String role);
}
