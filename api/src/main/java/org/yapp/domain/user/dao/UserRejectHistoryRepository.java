package org.yapp.domain.user.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.user.UserRejectHistory;

@Repository
public interface UserRejectHistoryRepository extends JpaRepository<UserRejectHistory, Long> {

    Optional<UserRejectHistory> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
