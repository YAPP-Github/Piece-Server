package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ErrorCode {
    NOTFOUND_MATCH(HttpStatus.NOT_FOUND, "Match not found"),
    MATCH_NOT_ACCEPTED(HttpStatus.BAD_REQUEST, "Match not accepted"),
    INVALID_MATCH_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 매칭 접근입니다."),

    NOT_ENOUGH_USER_FOR_INSTANT_MATCH(HttpStatus.BAD_REQUEST, "즉시매칭에 필요한 유저가 부족합니다"),
    NO_FREE_MATCH_TODAY(HttpStatus.BAD_REQUEST, "오늘의 무료 즉시 매칭을 사용했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
