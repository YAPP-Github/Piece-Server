package org.yapp.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    OAUTH_ERROR(HttpStatus.FORBIDDEN, "Oauth Error"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
