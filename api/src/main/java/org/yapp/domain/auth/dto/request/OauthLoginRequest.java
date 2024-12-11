package org.yapp.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OauthLoginRequest {
    private String providerName;
    private String token;
}
