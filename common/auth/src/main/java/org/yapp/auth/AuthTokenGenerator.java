package org.yapp.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yapp.jwt.JwtUtil;

@Component
@RequiredArgsConstructor
public class AuthTokenGenerator {

    private final JwtUtil jwtUtil;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;


    public AuthToken generate(Long userId, String oauthId, String role) {
        String accessToken = jwtUtil.createJwt("access_token", userId, oauthId, role,
            accessTokenExpiration);

        String refreshToken = jwtUtil.createJwt("refesh_token", userId, oauthId, role,
            refreshTokenExpiration);

        return new AuthToken(accessToken, refreshToken);
    }
}
