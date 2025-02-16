package org.yapp.domain.auth.application.oauth.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.AuthToken;
import org.yapp.core.auth.AuthTokenGenerator;
import org.yapp.core.auth.token.RefreshTokenService;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.application.oauth.OauthProviderResolver;
import org.yapp.domain.auth.presentation.dto.request.OauthLoginRequest;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.user.dao.UserRepository;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final OauthProviderResolver oauthProviderResolver;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthTokenGenerator authTokenGenerator;
    private final SettingService settingService;

    public OauthLoginResponse login(OauthLoginRequest request) {
        OauthProvider oauthProvider = oauthProviderResolver.find(request.getProviderName());
        String oauthId =
            request.getProviderName() + oauthProvider.getOAuthProviderUserId(
                request.getOauthCredential());

        //이미 가입된 유저인지 확인하고 가입되어 있지 않으면 회원가입 처리
        Optional<User> userOptional = userRepository.findByOauthId(oauthId);
        if (userOptional.isEmpty()) {
            User newUser = User.builder().oauthId(oauthId).role(RoleStatus.NONE.getStatus())
                .build();
            User savedUser = userRepository.save(newUser);
            Long userId = savedUser.getId();
            AuthToken token = authTokenGenerator.generate(userId, savedUser.getOauthId(), "NONE");
            String accessToken = token.accessToken();
            String refreshToken = token.refreshToken();
            settingService.createSetting(userId);
            refreshTokenService.saveRefreshToken(userId, refreshToken);
            return new OauthLoginResponse(RoleStatus.NONE.getStatus(), accessToken, refreshToken);
        }

        //이미 가입한 유저인 경우 로그인 처리
        Long userId = userOptional.get().getId();
        final User user = userOptional.get();

        AuthToken token = authTokenGenerator.generate(userId, user.getOauthId(), user.getRole());
        String accessToken = token.accessToken();
        String refreshToken = token.refreshToken();
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        //아직 SMS 인증이 완료되지 않음
        if (user.getRole().equals("NONE")) {
            return new OauthLoginResponse(RoleStatus.NONE.getStatus(), accessToken, refreshToken);
        }

        // SMS 인증은 완료되었으나, 프로필 등록을 안함
        if (user.getRole().equals("REGISTER")) {
            return new OauthLoginResponse(RoleStatus.REGISTER.getStatus(), accessToken,
                refreshToken);
        }

        //프로필 등록은 했지만, 심사중
        if (user.getRole().equals("PENDING")) {
            return new OauthLoginResponse(RoleStatus.PENDING.getStatus(), accessToken,
                refreshToken);
        }

        // 가입과 프로필 등록까지 완료된 유저
        return new OauthLoginResponse(user.getRole(), accessToken, refreshToken);
    }


    public OauthLoginResponse tmpTokenGet(Long userId) {
        //이미 가입된 유저인지 확인하고 가입되어 있지 않으면 회원가입 처리
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.get();

        AuthToken token = authTokenGenerator.generate(userId, user.getOauthId(), user.getRole());
        String accessToken = token.accessToken();
        String refreshToken = token.refreshToken();
        refreshTokenService.saveRefreshToken(userId, refreshToken);

        return new OauthLoginResponse(RoleStatus.NONE.getStatus(), accessToken, refreshToken);
    }
}
