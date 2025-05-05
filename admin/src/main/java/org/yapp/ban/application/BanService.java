package org.yapp.ban.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.auth.dao.BannedUserPhoneNumberRepository;
import org.yapp.core.auth.token.RefreshTokenService;
import org.yapp.core.domain.user.BannedUserPhoneNumber;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.user.application.UserService;

@Service
@RequiredArgsConstructor
public class BanService {

  private final BannedUserPhoneNumberRepository bannedUserPhoneNumberRepository;
  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final BanModifyingService banModifyingService;

  @Transactional
  public void banUser(Long userId) {
    User user = userService.getUserById(userId);
    user.updateUserRole(RoleStatus.BANNED.getStatus());
    bannedUserPhoneNumberRepository.save(new BannedUserPhoneNumber(user.getPhoneNumber()));
    refreshTokenService.deleteRefreshToken(userId);
    banModifyingService.addUserToBlackList(userId);
  }
}
