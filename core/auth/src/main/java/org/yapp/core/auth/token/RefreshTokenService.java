package org.yapp.core.auth.token;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.auth.AuthToken;
import org.yapp.core.auth.AuthTokenGenerator;
import org.yapp.core.auth.dao.RefreshTokenRepository;
import org.yapp.core.auth.dto.RefreshedTokens;
import org.yapp.core.auth.jwt.JwtUtil;
import org.yapp.core.domain.auth.RefreshToken;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.SecurityErrorCode;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;
  private final AuthTokenGenerator authTokenGenerator;

  public RefreshToken getUserRefreshToken(Long userId) {
    return refreshTokenRepository.findById(userId)
        .orElseThrow(() -> new ApplicationException(SecurityErrorCode.MISSING_REFRESH_TOKEN));
  }

  @Transactional
  public RefreshedTokens getUserRefreshedTokens(String refreshToken) {
    checkRefreshTokenExpiration(refreshToken);

    Long userId = jwtUtil.getUserId(refreshToken);
    String oauthId = jwtUtil.getOauthId(refreshToken);
    String role = jwtUtil.getRole(refreshToken);

    String expectedRefreshToken = getUserRefreshToken(userId).getToken();
    validateRefreshToken(refreshToken, expectedRefreshToken);

    AuthToken token = authTokenGenerator.generate(userId, oauthId, role);
    saveRefreshToken(userId, token.refreshToken());

    return new RefreshedTokens(token.accessToken(), token.refreshToken());
  }

  @Transactional
  public void saveRefreshToken(Long userId, String refreshToken) {
    Optional<RefreshToken> token = refreshTokenRepository.findById(userId);
    if (token.isPresent()) {
      RefreshToken currentRefreshToken = token.get();
      currentRefreshToken.updateToken(refreshToken);
      return;
    }
    refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
  }

  @Transactional
  public void deleteRefreshToken(Long userId) {
    refreshTokenRepository.deleteById(userId);
  }

  private void validateRefreshToken(String givenRefreshToken, String expectedRefreshToken) {
    if (!expectedRefreshToken.equals(givenRefreshToken)) {
      throw new ApplicationException(SecurityErrorCode.INVALID_REFRESH_TOKEN);
    }
  }

  private void checkRefreshTokenExpiration(String refreshToken) {
    try {
      jwtUtil.isExpired(refreshToken);
    } catch (Exception e) {
      throw new ApplicationException(SecurityErrorCode.EXPIRED_REFRESH_TOKEN);
    }
  }
}
