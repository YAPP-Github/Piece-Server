package org.yapp.exception.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    // Authentication Errors
    MISSING_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "Access token is missing."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid access token."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access token has expired."),

    MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh token is missing."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid refresh token."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token has expired."),

    INVALID_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "Authorization header is invalid."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "Unsupported token format."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "Invalid JWT signature."),

    // Authorization Errors
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is denied."),
    ROLE_NOT_ALLOWED(HttpStatus.FORBIDDEN,
        "User does not have the required role to access this resource."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
