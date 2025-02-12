package org.yapp.profile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileStatus;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.user.UserRejectHistory;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.profile.dao.UserRejectHistoryRepository;
import org.yapp.user.application.UserService;

@Service
@RequiredArgsConstructor
public class AdminProfileService {

    private final UserRejectHistoryRepository userRejectHistoryRepository;
    private final UserService userService;

    @Transactional
    public void updateProfileStatus(Long userId, boolean reasonImage, boolean reasonDescription) {
        User user = userService.getUserById(userId);
        Profile profile = user.getProfile();

        if (profile == null) {
            throw new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE);
        }

        if (reasonImage || reasonDescription) {
            rejectProfile(user.getProfile(), reasonImage, reasonDescription);
        } else {
            passProfile(profile);
        }
    }

    private void rejectProfile(Profile profile, boolean reasonImage, boolean reasonDescription) {
        userRejectHistoryRepository.save(UserRejectHistory.builder()
            .user(profile.getUser())
            .reasonImage(reasonImage)
            .reasonDescription(reasonDescription)
            .build());

        profile.updateProfileStatus(ProfileStatus.REJECTED);
        profile.getUser().updateUserRole(RoleStatus.PENDING.getStatus());
    }

    private void passProfile(Profile profile) {
        profile.updateProfileStatus(ProfileStatus.APPROVED);
        profile.getUser().updateUserRole(RoleStatus.USER.getStatus());
    }
}
