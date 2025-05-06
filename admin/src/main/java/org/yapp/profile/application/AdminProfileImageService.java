package org.yapp.profile.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileImage;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.core.exception.error.code.ProfileImageErrorCode;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.notification.application.AdminNotificationService;
import org.yapp.profile.dao.ProfileImageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final AdminNotificationService adminNotificationService;

    @Transactional(readOnly = true)
    public ProfileImage getByProfileImageId(Long profileImageId) {
        return profileImageRepository.findById(profileImageId)
            .orElseThrow(
                () -> new ApplicationException(ProfileImageErrorCode.PROFILE_IMAGE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public User getUserByProfileImageId(Long profileImageId) {
        ProfileImage profileImage = this.getByProfileImageId(profileImageId);
        Profile profile = profileImage.getProfile();

        if (profile == null) {
            throw new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE);
        }

        User user = profile.getUser();
        if (user == null) {
            throw new ApplicationException(UserErrorCode.NOTFOUND_USER);
        }

        return user;
    }

    @Transactional
    public void acceptProfileImage(Long profileImageId) {
        ProfileImage profileImage = getByProfileImageId(profileImageId);
        User user = getUserByProfileImageId(profileImageId);

        profileImage.accept();
        try {
            adminNotificationService.sendProfileImageApprovedNotification(user.getId());
        } catch (ApplicationException e) {
            log.warn("프로필 이미지 변경 FCM 알림 전송이 실패했습니다.", e);
        }
    }

    @Transactional
    public void rejectProfileImage(Long profileImageId) {
        ProfileImage profileImage = getByProfileImageId(profileImageId);
        User user = getUserByProfileImageId(profileImageId);

        profileImage.reject();
        try {
            adminNotificationService.sendProfileImageRejectedNotification(user.getId());
        } catch (ApplicationException e) {
            log.warn("프로필 이미지 변경 FCM 알림 전송이 실패했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public ProfileImage getLatestProfileImageByProfileId(Long profileId) {
        Optional<ProfileImage> optionalProfileImage = profileImageRepository.findTopByProfileIdOrderByCreatedAtDesc(
            profileId);

        return optionalProfileImage.orElse(null);
    }
}
