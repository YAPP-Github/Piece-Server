package org.yapp.domain.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthType {
    GOOGLE("google"),
    KAKAO("kakao");

    private final String typeName;
}
