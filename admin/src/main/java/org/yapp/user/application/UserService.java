package org.yapp.user.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileImage;
import org.yapp.core.domain.profile.ProfileImageStatus;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.user.UserRejectHistory;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.format.PageResponse;
import org.yapp.profile.application.AdminProfileImageService;
import org.yapp.profile.dao.ProfileValueTalkRepository;
import org.yapp.user.dao.UserRejectHistoryRepository;
import org.yapp.user.dao.UserRepository;
import org.yapp.user.presentation.response.UserProfileDetailResponses;
import org.yapp.user.presentation.response.UserProfileImageDetailResponse;
import org.yapp.user.presentation.response.UserProfileValidationResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AdminProfileImageService adminProfileImageService;
    private final UserRepository userRepository;
    private final UserRejectHistoryRepository userRejectHistoryRepository;
    private final ProfileValueTalkRepository profileValueTalkRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
    }

    public Optional<User> getUserByIdIfExists(Long userId) {
        return userRepository.findById(userId);
    }

    public User getUserByOauthId(String oauthId) {
        return userRepository.findByOauthId(oauthId)
            .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
    }

    public PageResponse<UserProfileValidationResponse> getUserProfilesWithPagination(int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserProfileValidationResponse> content = userPage.getContent().stream()
            .map(user -> {
                Optional<UserRejectHistory> optionalProfileRejectHistory =
                    userRejectHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(
                        user.getId());

                boolean reasonImage = optionalProfileRejectHistory.map(
                    UserRejectHistory::isReasonImage).orElse(false);
                boolean reasonDescription = optionalProfileRejectHistory.map(
                    UserRejectHistory::isReasonDescription).orElse(false);

                Profile profile = user.getProfile();
                boolean isApproved = profile != null
                    && profile.getProfileStatus() != null
                    && profile.getProfileStatus().name().equals("APPROVED");

                ProfileImage profileImage = null;
                ProfileImageStatus profileImageStatus = null;
                if (profile != null) {
                    profileImage = adminProfileImageService.getLatestProfileImageByProfileId(
                        profile.getId());
                }
                if (profileImage != null) {
                    profileImageStatus = profileImage.getStatus();
                }

                return UserProfileValidationResponse.from(user,
                    profileImageStatus, !isApproved && reasonImage,
                    !isApproved && reasonDescription);
            })
            .toList();

        return new PageResponse<>(
            content,
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalPages(),
            userPage.getTotalElements(),
            userPage.isFirst(),
            userPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public UserProfileDetailResponses getUserProfileDetails(Long userId) {
        User user = getUserById(userId);
        Profile profile = user.getProfile();

        if (profile == null) {
            throw new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE);
        }

        List<ProfileValueTalk> activeProfileValueTalks = profileValueTalkRepository.findActiveProfileValueTalksByProfileId(
            profile.getId());

        return UserProfileDetailResponses.from(profile.getProfileBasic().getNickname(),
            profile.getProfileBasic().getImageUrl(),
            activeProfileValueTalks);
    }

    @Transactional(readOnly = true)
    public UserProfileImageDetailResponse getUserProfileImageDetails(Long userId) {
        User user = getUserById(userId);
        Profile profile = user.getProfile();

        if (profile == null) {
            throw new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE);
        }

        ProfileImage profileImage = adminProfileImageService.getLatestProfileImageByProfileId(
            profile.getId());

        return UserProfileImageDetailResponse.from(profileImage,
            profile.getProfileBasic().getImageUrl());
    }
}
