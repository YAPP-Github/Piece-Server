package org.yapp.profile.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.profile.ProfileImage;
import org.yapp.core.domain.profile.ProfileImageStatus;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    List<ProfileImage> findByProfileId(Long profileId);

    List<ProfileImage> findByProfileIdAndStatus(Long profileId, ProfileImageStatus status);

    Optional<ProfileImage> findTopByProfileIdOrderByCreatedAtDesc(Long profileId);
}