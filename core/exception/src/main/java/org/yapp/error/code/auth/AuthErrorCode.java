package org.yapp.error.code.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.yapp.error.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    OAUTH_ERROR(HttpStatus.FORBIDDEN, "Oauth Error"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}
