package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QdrantErrorCode implements ErrorCode {
    QDRANT_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "Qdrant가 정상적으로 동작하고 있지 않습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
