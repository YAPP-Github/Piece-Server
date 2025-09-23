package org.yapp.domain.user.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.auth.AuthToken;
import org.yapp.core.auth.AuthTokenGenerator;
import org.yapp.core.auth.AuthUtil;
import org.yapp.core.auth.dao.BannedUserPhoneNumberRepository;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.domain.fcm.FcmToken;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.user.UserDeleteReason;
import org.yapp.core.domain.user.UserRejectHistory;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.core.notification.dao.FcmTokenRepository;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.auth.presentation.dto.response.SmsVerifyResponse;
import org.yapp.domain.user.dao.UserDeleteReasonRepository;
import org.yapp.domain.user.dao.UserRejectHistoryRepository;
import org.yapp.domain.user.dao.UserRepository;
import org.yapp.domain.user.presentation.dto.request.FcmTokenSaveRequest;
import org.yapp.domain.user.presentation.dto.response.UserBasicInfoResponse;
import org.yapp.domain.user.presentation.dto.response.UserRejectHistoryResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRejectHistoryRepository userRejectHistoryRepository;
    private final UserDeleteReasonRepository userDeleteReasonRepository;
    private final AuthTokenGenerator authTokenGenerator;
    private final FcmTokenRepository fcmTokenRepository;
    private final BannedUserPhoneNumberRepository bannedUserPhoneNumberRepository;
    private final JwtUtil jwtUtil;

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
    public SmsVerifyResponse registerPhoneNumber(Long userId, String phoneNumber) {
        Optional<User> userOptionalByPhoneNumber = this.getUserByPhoneNumber(phoneNumber);

        // 이미 가입한 유저
        if (userOptionalByPhoneNumber.isPresent()) {
            String userOauthProvider = this.getUserOauthProvider(
                userOptionalByPhoneNumber.get().getOauthId());

            return new SmsVerifyResponse(null, null, null, true, userOauthProvider);
        }

        // 정지된 유저가 기존 계정 삭제하고 다시 가입하려고 할 때 방지
        if (bannedUserPhoneNumberRepository.findById(phoneNumber).isPresent()) {
            throw new ApplicationException(AuthErrorCode.PERMANENTLY_BANNED);
        }

        User user =
            userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));

        user.updateUserRole(RoleStatus.REGISTER.getStatus());
        user.initializePhoneNumber(phoneNumber);
        String oauthId = user.getOauthId();
        AuthToken authToken = authTokenGenerator.generate(userId, oauthId, user.getRole());
        return new SmsVerifyResponse(RoleStatus.REGISTER.getStatus(), authToken.accessToken(),
            authToken.refreshToken(), false, null);
    }

    @Transactional(readOnly = true)
    public UserRejectHistoryResponse getUserRejectHistoryLatest(Long userId) {
        boolean reasonImage = false;
        boolean reasonDescription = false;

        UserRejectHistory userRejectHistory = userRejectHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(
            userId).orElse(null);

        if (userRejectHistory != null) {
            reasonImage = userRejectHistory.isReasonImage();
            reasonDescription = userRejectHistory.isReasonDescription();
        }

        return new UserRejectHistoryResponse(
            reasonImage,
            reasonDescription
        );
    }

    @Transactional
    public void deleteUser(Long userId, String reason) {
        userDeleteReasonRepository.save(new UserDeleteReason(userId, reason));
        userRepository.deleteById(userId);
    }

    public UserBasicInfoResponse getUserBasicInfo(Long userId, String authorizationHeader) {
        User user = this.getUserById(userId);

        Profile profile = user.getProfile();
        String profileStatus =
            profile != null ? profile.getProfileStatus().toString() : null;

        String accessToken = AuthUtil.extractAccessToken(authorizationHeader);
        String accessTokenRole = jwtUtil.getRole(accessToken);
        String userRole = user.getRole();

        if (!accessTokenRole.equals(userRole)) {
            AuthToken authToken = authTokenGenerator.generate(userId, user.getOauthId(),
                user.getRole());
            return new UserBasicInfoResponse(userId, userRole, profileStatus, true,
                authToken.accessToken(), authToken.refreshToken());
        }

        return new UserBasicInfoResponse(userId, userRole, profileStatus, false, null, null);
    }

    @Transactional
    public void saveFcmToken(Long userId, FcmTokenSaveRequest request) {
        Optional<FcmToken> fcmTokenOptional = fcmTokenRepository.findByUserId(userId);
        if (fcmTokenOptional.isPresent()) {
            FcmToken fcmToken = fcmTokenOptional.get();
            fcmToken.updateToken(request.getToken());
        } else {
            FcmToken fcmToken = new FcmToken(userId, request.getToken());
            fcmTokenRepository.save(fcmToken);
        }
    }

    @Transactional
    public void deleteFcmToken(Long userId) {
        fcmTokenRepository.deleteByUserId(userId);
    }

    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public String getUserOauthProvider(String oauthId) {
        if (oauthId.startsWith("kakao")) {
            return "kakao";
        } else if (oauthId.startsWith("apple")) {
            return "apple";
        } else if (oauthId.startsWith("google")) {
            return "google";
        } else {
            throw new ApplicationException(UserErrorCode.INVALID_OAUTH_PROVIDER);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Long getUserPuzzleCount(Long userId) {
        User user = getUserById(userId);
        return user.getPuzzleWallet() != null ? user.getPuzzleWallet().getPuzzleCount() : 0L;
    }

    @Transactional(readOnly = true)
    public boolean checkUserHasAdminFlag(Long adminUserId) {
        User user = this.getUserById(adminUserId);
        return Boolean.TRUE.equals(user.getIsAdmin());
    }
}
