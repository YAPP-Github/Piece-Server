package org.yapp.domain.block.application;

import java.util.List;
import java.util.stream.Collectors;
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
  private final BloomBlockService bloomBlockService;

  @Transactional(readOnly = true)
  public List<Long> getDirectBlockIds(Long blockingUserId) {
    List<DirectBlock> blocks = directBlockRepository.findAllByBlockingUserId(
        blockingUserId);
    return blocks.stream().map(DirectBlock::getId).collect(Collectors.toList());
  }

  @Transactional
  public DirectBlock blockMatchedUser(Long userId, Long matchId) {
    MatchInfo matchInfo = matchInfoRepository.findById(matchId)
        .orElseThrow(() -> new ApplicationException(MatchErrorCode.NOTFOUND_MATCH));

    matchInfo.blockMatch(userId);

    return blockUser(userId, matchInfo.getPartnerUserId(userId));
  }

  @Transactional
  public DirectBlock blockUser(Long userId, Long blockId) {
    bloomBlockService.blockUserId(userId, blockId);
    return directBlockRepository.save(new DirectBlock(userId, blockId));
  }

  public boolean checkBlock(Long userId, Long partnerId) {
    return directBlockRepository.existsDirectBlockByBlockingUserIdAndBlockedUserId(userId,
        partnerId);
  }

}
