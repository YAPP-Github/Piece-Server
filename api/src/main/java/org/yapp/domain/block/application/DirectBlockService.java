package org.yapp.domain.block.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.block.DirectBlock;
import org.yapp.core.domain.match.MatchInfo;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.MatchErrorCode;
import org.yapp.domain.block.dao.DirectBlockRepository;
import org.yapp.domain.match.dao.MatchInfoRepository;

@Service
@RequiredArgsConstructor
public class DirectBlockService {

    private final DirectBlockRepository directBlockRepository;
    private final MatchInfoRepository matchInfoRepository;

    @Transactional
    public DirectBlock blockMatchedUser(Long userId, Long matchId) {
        MatchInfo matchInfo = matchInfoRepository.findById(matchId)
            .orElseThrow(() -> new ApplicationException(MatchErrorCode.NOTFOUND_MATCH));

        matchInfo.blockMatch(userId);

        return blockUser(userId, matchInfo.getPartnerUserId(userId));
    }

    @Transactional
    public DirectBlock blockUser(Long userId, Long blockId) {
        return directBlockRepository.save(new DirectBlock(userId, blockId));
    }

    public boolean checkBlock(Long userId, Long partnerId) {
        return directBlockRepository.existsDirectBlockByBlockingUserIdAndBlockedUserId(userId,
            partnerId);
    }

}
