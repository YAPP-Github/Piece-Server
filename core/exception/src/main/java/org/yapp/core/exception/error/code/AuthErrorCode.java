package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    OAUTH_ERROR(HttpStatus.FORBIDDEN, "OAuth 오류"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    OAUTH_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "OAuth ID를 찾을 수 없습니다."),
    ID_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ID 토큰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}