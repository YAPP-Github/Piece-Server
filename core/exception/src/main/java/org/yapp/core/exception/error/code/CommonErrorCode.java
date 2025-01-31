package org.yapp.core.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 400 BAD_REQUEST
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication required"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "You do not have permission to access this resource"),

    // 404 NOT_FOUND
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus httpStatus;
    private final String message;
}
