package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileErrorCode implements ErrorCode {
    INACTIVE_PROFILE(HttpStatus.FORBIDDEN, "비활성화 프로필입니다."),
    NOTFOUND_PROFILE(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
