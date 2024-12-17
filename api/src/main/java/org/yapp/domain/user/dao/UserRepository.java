package org.yapp.domain.user.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthId(String oauthId);
}
