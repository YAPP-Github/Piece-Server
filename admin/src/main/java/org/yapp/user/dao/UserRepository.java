package org.yapp.user.dao;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByProfileId(Long profileId);

    Optional<User> findByOauthId(String oauthId);

    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.profileBasic.nickname = :nickname")
    Optional<User> findByProfileNickname(String nickname);
    
    Page<User> findAllByRole(String role, Pageable pageable);
}
