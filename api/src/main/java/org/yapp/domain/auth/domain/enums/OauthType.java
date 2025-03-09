package org.yapp.domain.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OauthType {
    GOOGLE("google"),
    KAKAO("kakao"),
    APPLE("apple");

    private final String typeName;
}
