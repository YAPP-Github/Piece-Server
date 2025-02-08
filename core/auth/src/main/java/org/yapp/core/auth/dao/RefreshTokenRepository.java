package org.yapp.core.auth.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.auth.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByUserId(Long userId);
}
