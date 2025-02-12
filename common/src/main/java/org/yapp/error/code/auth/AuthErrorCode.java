package org.yapp.error.code.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.yapp.error.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    OAUTH_ERROR(HttpStatus.FORBIDDEN, "Oauth Error"),
    OAUTH_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Oauth ID Not Found"),
    ID_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ID token not found");


    private final HttpStatus httpStatus;
    private final String message;
}
