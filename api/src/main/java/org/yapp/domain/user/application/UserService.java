package org.yapp.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.auth.AuthToken;
import org.yapp.core.auth.AuthTokenGenerator;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.user.UserDeleteReason;
import org.yapp.core.domain.user.UserRejectHistory;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.user.dao.UserDeleteReasonRepository;
import org.yapp.domain.user.dao.UserRejectHistoryRepository;
import org.yapp.domain.user.dao.UserRepository;
import org.yapp.domain.user.presentation.dto.response.UserBasicInfoResponse;
import org.yapp.domain.user.presentation.dto.response.UserRejectHistoryResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRejectHistoryRepository userRejectHistoryRepository;
    private final UserDeleteReasonRepository userDeleteReasonRepository;
    private final AuthTokenGenerator authTokenGenerator;

    /**
     * Role을 USER로 바꾸고 변경된 토큰을 반환한다.
     *
     * @return 액세스토큰과 리프레시 토큰
     */
    @Transactional
    public OauthLoginResponse completeProfileInitialize(Long userId, Profile profile) {
        User user =
            userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
        user.setProfile(profile);
        user.updateUserRole(RoleStatus.PENDING.getStatus());
        String oauthId = user.getOauthId();
        AuthToken authToken = authTokenGenerator.generate(userId, oauthId, user.getRole());
        return new OauthLoginResponse(RoleStatus.PENDING.getStatus(), authToken.accessToken(),
            authToken.refreshToken());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
    }

    /**
     * Role을 Register로 바꾸고 변경된 토큰을 반환한다.
     *
     * @return 액세스토큰과 리프레시 토큰
     */
    @Transactional
    public OauthLoginResponse registerPhoneNumber(Long userId, String phoneNumber) {
        User user =
            userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
        user.updateUserRole(RoleStatus.REGISTER.getStatus());
        user.initializePhoneNumber(phoneNumber);
        String oauthId = user.getOauthId();
        AuthToken authToken = authTokenGenerator.generate(userId, oauthId, user.getRole());
        return new OauthLoginResponse(RoleStatus.REGISTER.getStatus(), authToken.accessToken(),
            authToken.refreshToken());
    }

    @Transactional(readOnly = true)
    public UserRejectHistoryResponse getUserRejectHistoryLatest(Long userId) {
        User user = this.getUserById(userId);
        Profile profile = user.getProfile();

        boolean reasonImage = false;
        boolean reasonDescription = false;

        UserRejectHistory userRejectHistory = userRejectHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(
            userId).orElse(null);

        if (userRejectHistory != null) {
            reasonImage = userRejectHistory.isReasonImage();
            reasonDescription = userRejectHistory.isReasonDescription();
        }

        return new UserRejectHistoryResponse(
            profile.getProfileStatus(),
            reasonImage,
            reasonDescription
        );
    }

    @Transactional
    public void deleteUser(Long userId, String reason) {
        userDeleteReasonRepository.save(new UserDeleteReason(userId, reason));
        userRepository.deleteById(userId);
    }

    public UserBasicInfoResponse getUserBasicInfo(Long userId) {
        User user = this.getUserById(userId);

        Profile profile = user.getProfile();

        String profileStatus =
            profile != null ? profile.getProfileStatus().toString() : null;

        return new UserBasicInfoResponse(userId, user.getRole(), profileStatus);
    }
}
