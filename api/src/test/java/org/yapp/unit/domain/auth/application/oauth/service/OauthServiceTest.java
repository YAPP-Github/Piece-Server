package org.yapp.unit.domain.auth.application.oauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.yapp.core.auth.AuthToken;
import org.yapp.core.auth.AuthTokenGenerator;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.auth.token.RefreshTokenService;
import org.yapp.core.domain.user.User;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.application.oauth.OauthProviderResolver;
import org.yapp.domain.auth.application.oauth.service.OauthService;
import org.yapp.domain.auth.presentation.dto.request.OauthLoginRequest;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.setting.application.SettingService;
import org.yapp.domain.user.dao.UserRepository;

@ExtendWith(MockitoExtension.class)
class OauthServiceTest {

    @InjectMocks
    private OauthService oauthService;
    @Mock
    private OauthProviderResolver oauthProviderResolver;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OauthProvider oauthProvider;
    @Mock
    private AuthTokenGenerator authTokenGenerator;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private SettingService settingService;

    @Test
    @DisplayName("처음 로그인에 성공하여 회원가입을 한 뒤 액세스,리프레시 토큰을 반환한다")
    void testFirstLogin_Success() {
        //given
        String providerName = "provider";
        String token = "oauth_token";
        OauthLoginRequest request = new OauthLoginRequest(providerName, token);
        String oauthId = providerName + "123456";

        when(oauthProviderResolver.find(providerName)).thenReturn(oauthProvider);
        when(oauthProvider.getOAuthProviderUserId(token)).thenReturn("123456");
        when(userRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());
        doNothing().when(settingService).createSetting(any(Long.class));
        User user = User.builder().oauthId(oauthId).build();
        ReflectionTestUtils.setField(user, "id", 1L);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(authTokenGenerator.generate(1L, oauthId, "NONE")).thenReturn(
            new AuthToken("access_token", "refresh_token"));

        // When
        OauthLoginResponse response = oauthService.login(request);

        // Then
        assertThat(response.getAccessToken()).isEqualTo("access_token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh_token");
    }

    @Test
    @DisplayName("존재하지 않는 Provider로 로그인을 요청하여 로그인에 실패한다.")
    void testLogin_Failure() {
        // Given
        String providerName = "nonexistent";
        String token = "oauth_token";
        OauthLoginRequest request = new OauthLoginRequest(providerName, token);

        // Mocking
        when(oauthProviderResolver.find(providerName)).thenThrow(new RuntimeException());

        // When & Then
        assertThrows(RuntimeException.class, () -> oauthService.login(request));
    }

    @Test
    @DisplayName("회원가입이 된 상태에서 로그인에 성공하여 액세스, 리프레시 토큰을 반환한다.")
    void testNotFirstLogin_Success() {
        //given
        String providerName = "provider";
        String token = "oauth_token";
        OauthLoginRequest request = new OauthLoginRequest(providerName, token);
        String oauthId = providerName + "123456";

        when(oauthProviderResolver.find(providerName)).thenReturn(oauthProvider);
        when(oauthProvider.getOAuthProviderUserId(token)).thenReturn("123456");
        User existingUser = User.builder().oauthId(oauthId).build();
        ReflectionTestUtils.setField(existingUser, "id", 1L);
        ReflectionTestUtils.setField(existingUser, "role", "member");
        when(userRepository.findByOauthId(oauthId)).thenReturn(Optional.of(existingUser));
        when(authTokenGenerator.generate(1L, oauthId, "member")).thenReturn(
            new AuthToken("access_token", "refresh_token"));

        // When
        OauthLoginResponse response = oauthService.login(request);

        // Then
        assertThat(response.getAccessToken()).isEqualTo("access_token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh_token");
    }
}