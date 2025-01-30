package org.yapp.domain.user.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(String oauthId);
}
