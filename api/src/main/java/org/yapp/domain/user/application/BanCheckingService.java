package org.yapp.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.auth.token.TokenHealthCheckService;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class BanCheckingService {

    private static final String USER_BLACK_LIST_SET = "user_blacklist";
    
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final TokenHealthCheckService tokenHealthCheckService;

    public void checkBlackListByRefreshToken(String refreshToken) {
        tokenHealthCheckService.healthCheck(refreshToken);
        Long userId = jwtUtil.getUserId(refreshToken);

        boolean isBlocked = existsInBlackList(userId);
        if (isBlocked) {
            throw new ApplicationException(AuthErrorCode.PERMANENTLY_BANNED);
        }
    }

    public boolean existsInBlackList(Long userId) {
        return redisService.existsInSet(USER_BLACK_LIST_SET, String.valueOf(userId));
    }
}
