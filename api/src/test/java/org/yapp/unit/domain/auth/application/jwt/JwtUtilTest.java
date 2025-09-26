package org.yapp.unit.domain.auth.application.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yapp.core.auth.jwt.JwtUtil;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        String secret = "mytestsecretkey123456789012345678910";
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    @DisplayName("사용자의 정보로 JWT 토큰을 만들고 다시 정보를 해석한다.")
    void testCreateJwtAndParseClaims() {
        // given
        String category = "access_token";
        Long userId = 123L;
        String oauthId = "kakao12345";
        String role = "USER";
        Long expiredMs = 1000L * 60 * 60;

        // when
        String token = jwtUtil.createJwt(category, userId, oauthId, role, expiredMs);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtUtil.getCategory(token)).isEqualTo(category);
        assertThat(jwtUtil.getUserId(token)).isEqualTo(userId);
        assertThat(jwtUtil.getOauthId(token)).isEqualTo(oauthId);
        assertThat(jwtUtil.getRole(token)).isEqualTo(role);
        assertThat(jwtUtil.isExpired(token)).isFalse();
    }

    @Test
    @DisplayName("JWT 만료시간이 지나 예외가 발생한다")
    void testExpiredToken() throws InterruptedException {
        // given
        String token = jwtUtil.createJwt("auth", 123L, "oauth-test", "USER", 1L); // 1ms 만료 시간
        Thread.sleep(2);
        // when & then
        assertThrows(RuntimeException.class, () -> jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("잘못된 내용의 토큰이 들어왔을 때 예외를 발생시킨다.")
    void testInvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when & then
        assertThrows(RuntimeException.class, () -> jwtUtil.getUserId(invalidToken));
        assertThrows(RuntimeException.class, () -> jwtUtil.getOauthId(invalidToken));
        assertThrows(RuntimeException.class, () -> jwtUtil.getRole(invalidToken));
        assertThrows(RuntimeException.class, () -> jwtUtil.isExpired(invalidToken));
    }

    @Test
    @DisplayName("잘못된 형태의 토큰이 들어오면 실패한다")
    void testMalformedToken() {
        // given
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid-signature";

        // when & then
        assertThrows(RuntimeException.class, () -> jwtUtil.getUserId(malformedToken));
    }
}