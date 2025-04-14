package org.yapp.domain.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SmsVerifyResponse {

    private String role;
    private String accessToken;
    private String refreshToken;
    private Boolean isPhoneNumberDuplicated;
    private String oauthProvider;
}
