package org.yapp.domain.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsAuthResponse {

    private String phoneNumber;
}
