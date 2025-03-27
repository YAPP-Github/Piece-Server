package org.yapp.domain.block.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.block.BlockContact;
import org.yapp.domain.match.application.MatchService;

/**
 * Redis에 장애가 생긴 상황이나 마이그레이션 상황에 대비한 블룸필터 복구 서비스
 */
@RequiredArgsConstructor
@Service
public class BloomBlockRepairService {

  private final BloomBlockService bloomBlockService;
  private final DirectBlockService directBlockService;
  private final BlockContactService blockContactService;
  private final MatchService matchService;

  /**
   * ID 기반 블룸 필터 복구
   *
   * @param userId 유저 ID
   */
  public void repairBloomBlockById(Long userId) {
    List<Long> previouslyMatchedUserIds = matchService.getPreviouslyMatchedUserIds(userId);
    List<Long> directBlockIds = directBlockService.getDirectBlockIds(userId);

    bloomBlockService.blockUserIdList(userId, previouslyMatchedUserIds);
    bloomBlockService.blockUserIdList(userId, directBlockIds);
  }

  /**
   * 연락처 블룸 필터 복구
   *
   * @param userId 유저 ID
   */
  public void repairBloomBlockByContacts(Long userId) {
    List<BlockContact> blocksByUserId = blockContactService.findBlocksByUserId(userId);
    List<String> blockContacts = blocksByUserId.stream().map(BlockContact::getPhoneNumber).toList();
    bloomBlockService.blockContactList(userId, blockContacts);
  }
}
