package org.yapp.domain.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUserId(Long userId);
}
