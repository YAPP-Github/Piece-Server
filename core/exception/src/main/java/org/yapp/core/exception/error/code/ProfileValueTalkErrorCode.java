package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileValueTalkErrorCode implements ErrorCode {
    NOTFOUND_PROFILE_VALUE_TALK(HttpStatus.NOT_FOUND, "존재하지 않는 프로필 가치관 톡입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}