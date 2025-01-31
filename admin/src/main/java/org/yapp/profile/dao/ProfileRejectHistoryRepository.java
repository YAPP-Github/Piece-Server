package org.yapp.profile.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.profile.ProfileRejectHistory;

@Repository
public interface ProfileRejectHistoryRepository extends JpaRepository<ProfileRejectHistory, Long> {

    Optional<ProfileRejectHistory> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
