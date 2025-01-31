package org.yapp.domain.block.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.block.DirectBlock;

public interface DirectBlockRepository extends JpaRepository<DirectBlock, Long> {

    boolean existsDirectBlockByBlockingUserIdAndBlockedUserId(Long blockingUserId,
        Long blockedUserId);
}
