package org.yapp.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SmsAuthErrorCode implements ErrorCode {
    CODE_NOT_EXIST(HttpStatus.BAD_REQUEST, "code not exist"),
    CODE_NOT_CORRECT(HttpStatus.BAD_REQUEST, "code not correct");
    private final HttpStatus httpStatus;
    private final String message;
}
