package org.yapp.domain.setting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.setting.Setting;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.SettingErrorCode;
import org.yapp.domain.setting.dao.SettingRepository;
import org.yapp.domain.setting.dto.response.SettingInfoResponse;

@Service
@RequiredArgsConstructor
public class SettingService {

  private final SettingRepository settingRepository;

  @Transactional
  public void setNotificationStatus(Long userId, boolean status) {
    Setting userSetting = getUserSetting(userId);
    userSetting.updateNotification(status);
  }

  @Transactional
  public void setMatchNotificationStatus(Long userId, boolean status) {
    Setting userSetting = getUserSetting(userId);
    userSetting.updateMatchNotification(status);
  }

  @Transactional
  public void setAcquaintanceBlockStatus(Long userId, boolean status) {
    Setting userSetting = getUserSetting(userId);
    userSetting.updateAcquaintanceBlock(status);
  }

  @Transactional
  public void createSetting(Long userId) {
    Setting setting = new Setting(userId, true, false, true);
    settingRepository.save(setting);
  }

  public Setting getUserSetting(Long userId) {
    return settingRepository.findByUserId(userId).orElseThrow(() -> new ApplicationException(
        SettingErrorCode.NOT_FOUND_SETTING));
  }

  public boolean isUserNotificationEnabled(Long userId) {
    Setting userSetting = getUserSetting(userId);
    return userSetting.isNotification();
  }

  public boolean isMatchNotificationEnabled(Long userId) {
    Setting userSetting = getUserSetting(userId);
    return userSetting.isNotification() && userSetting.isMatchNotification();
  }

  public boolean isAcquaintanceBlockEnabled(Long userId) {
    Setting userSetting = getUserSetting(userId);
    return userSetting.isAcquaintanceBlock();
  }

  public SettingInfoResponse getUserSettingInfo(Long userId) {
    Setting userSetting = getUserSetting(userId);
    return new SettingInfoResponse(userSetting.isNotification(), userSetting.isMatchNotification(),
        userSetting.isAcquaintanceBlock());
  }
}
