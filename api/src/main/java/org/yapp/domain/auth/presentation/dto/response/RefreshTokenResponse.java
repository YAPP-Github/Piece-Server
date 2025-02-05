package org.yapp.domain.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RefreshTokenResponse {
  private String accessToken;
  private String refreshToken;
}
