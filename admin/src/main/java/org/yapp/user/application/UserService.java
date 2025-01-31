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
import org.yapp.core.domain.profile.ProfileRejectHistory;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.format.PageResponse;
import org.yapp.profile.dao.ProfileRejectHistoryRepository;
import org.yapp.profile.dao.ProfileValueTalkRepository;
import org.yapp.user.dao.UserRepository;
import org.yapp.user.presentation.response.UserProfileDetailResponses;
import org.yapp.user.presentation.response.UserProfileValidationResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRejectHistoryRepository profileRejectHistoryRepository;
    private final ProfileValueTalkRepository profileValueTalkRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
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
                Optional<ProfileRejectHistory> optionalProfileRejectHistory =
                    profileRejectHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(
                        user.getId());

                boolean reasonImage = optionalProfileRejectHistory.map(
                    ProfileRejectHistory::isReasonImage).orElse(false);
                boolean reasonDescription = optionalProfileRejectHistory.map(
                    ProfileRejectHistory::isReasonDescription).orElse(false);

                return UserProfileValidationResponse.from(user, reasonImage, reasonDescription);
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
}
