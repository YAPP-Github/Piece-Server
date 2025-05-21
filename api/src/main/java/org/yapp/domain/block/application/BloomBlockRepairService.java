package org.yapp.domain.block.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.block.BlockContact;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.domain.match.application.MatchService;
import org.yapp.domain.user.application.UserService;

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
  private final UserService userService;

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

  public void repairBloomBlockForAllUser(Long userId) {
    System.out.println(userId);
    User user = userService.getUserById(userId);
    if (!user.getIsAdmin()) {
      throw new ApplicationException(AuthErrorCode.ACCESS_DENIED);
    }
    userService.getAllUsers()
        .forEach(eachUser -> {
          repairBloomBlockById(eachUser.getId());
          repairBloomBlockByContacts(eachUser.getId());
        });
  }
}
