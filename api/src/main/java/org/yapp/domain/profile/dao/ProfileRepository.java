package org.yapp.domain.profile.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.profile.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByProfileBasic_Nickname(String nickname);

    List<Profile> findByProfileBasic_LocationAndUser_RoleAndUser_IsAdminIsNull(
        String profileBasicLocation, String userRole);

    List<Profile> findAllByUser_IdIn(List<Long> ids);

    List<Profile> findAllByUser_RoleAndUser_IsAdminIsNull(String userRole);
}