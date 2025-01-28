package org.yapp.error.code.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.yapp.error.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum SmsAuthErrorCode implements ErrorCode {
    CODE_NOT_EXIST(HttpStatus.BAD_REQUEST, "code not exist"),
    CODE_NOT_CORRECT(HttpStatus.BAD_REQUEST, "code not correct");
    private final HttpStatus httpStatus;
    private final String message;
}
