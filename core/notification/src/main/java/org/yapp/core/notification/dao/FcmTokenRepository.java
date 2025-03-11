package org.yapp.core.notification.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.fcm.FcmToken;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

  Optional<FcmToken> findByUserId(Long userId);

  void deleteByUserId(Long userId);
}
