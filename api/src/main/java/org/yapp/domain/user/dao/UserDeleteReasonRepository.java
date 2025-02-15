package org.yapp.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.user.UserDeleteReason;

public interface UserDeleteReasonRepository extends JpaRepository<UserDeleteReason, Long> {

}
