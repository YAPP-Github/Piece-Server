package org.yapp.domain.block.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.auth.AuthenticationService;
import org.yapp.domain.block.DirectBlock;
import org.yapp.domain.block.dao.DirectBlockRepository;

@Service
@RequiredArgsConstructor
public class DirectBlockService {

    private final DirectBlockRepository directBlockRepository;
    private final AuthenticationService authenticationService;

    public DirectBlock blockUser(Long blockId) {
        Long userId = authenticationService.getUserId();
        return directBlockRepository.save(new DirectBlock(userId, blockId));
    }

    public boolean checkBlock(Long userId, Long partnerId) {
        return directBlockRepository.existsDirectBlockByBlockingUserIdAndBlockedUserId(userId,
            partnerId);
    }

}
