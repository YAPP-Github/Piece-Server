package org.yapp.domain.setting.application;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.setting.BlockContactSyncTime;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.BlockContactErrorCode;
import org.yapp.domain.setting.dao.BlockContactSyncTimeRepository;
import org.yapp.domain.setting.dto.response.BlockContactSyncTimeResponse;

@Service
@RequiredArgsConstructor
public class BlockContactSyncTimeService {

  private final BlockContactSyncTimeRepository blockContactSyncTimeRepository;

  @Transactional
  public void saveBlockContactSyncTime(Long userId, LocalDateTime now) {
    Optional<BlockContactSyncTime> blockContactSyncTimeOptional = blockContactSyncTimeRepository.findById(
        userId);
    if (blockContactSyncTimeOptional.isPresent()) {
      BlockContactSyncTime blockContactSyncTime = blockContactSyncTimeOptional.get();
      blockContactSyncTime.updateSyncTime(now);
      return;
    }
    BlockContactSyncTime blockContactSyncTime = new BlockContactSyncTime(userId, now);
    blockContactSyncTimeRepository.save(blockContactSyncTime);
  }

  public BlockContactSyncTimeResponse getBlockContactSyncTimeResponse(Long userId) {
    BlockContactSyncTime blockContactSyncTime = blockContactSyncTimeRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException(
            BlockContactErrorCode.BLOCK_SYNC_TIME_NOT_FOUND));
    return new BlockContactSyncTimeResponse(blockContactSyncTime.getSyncTime());
  }
}
