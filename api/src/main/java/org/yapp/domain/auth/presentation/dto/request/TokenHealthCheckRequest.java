package org.yapp.domain.auth.presentation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TokenHealthCheckRequest {

  private String token;
}
