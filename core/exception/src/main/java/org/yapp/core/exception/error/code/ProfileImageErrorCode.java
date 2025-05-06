package org.yapp.core.exception.error.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfileImageErrorCode implements ErrorCode {
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 이미지가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
