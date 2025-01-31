package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchErrorCode implements ErrorCode {
    NOTFOUND_MATCH(HttpStatus.NOT_FOUND, "Match not found"),
    MATCH_NOT_ACCEPTED(HttpStatus.BAD_REQUEST, "Match not accepted");

    private final HttpStatus httpStatus;
    private final String message;
}
