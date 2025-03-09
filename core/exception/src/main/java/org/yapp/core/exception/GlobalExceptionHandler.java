package org.yapp.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.yapp.core.exception.error.code.AuthErrorCode;
import org.yapp.core.exception.error.code.CommonErrorCode;
import org.yapp.core.exception.error.code.ErrorCode;
import org.yapp.core.exception.error.response.ErrorResponse;
import org.yapp.infra.discord.DiscordMessageFactory;
import org.yapp.infra.discord.DiscordNotificationService;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private DiscordNotificationService discordNotificationService;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(final ApplicationException e,
        final HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        discordNotificationService.sendNotification(
            DiscordMessageFactory.createErrorMessage(request, e));
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationException(
        final AuthorizationDeniedException e, final HttpServletRequest request) {
        discordNotificationService.sendNotification(
            DiscordMessageFactory.createErrorMessage(request, e));
        return handleExceptionInternal(AuthErrorCode.ACCESS_DENIED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e,
        final HttpServletRequest request) {
        log.warn("handleIllegalArgument", e);
        discordNotificationService.sendNotification(
            DiscordMessageFactory.createErrorMessage(request, e));
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, e.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException e,
        final HttpHeaders headers,
        final HttpStatusCode status,
        final WebRequest request) {
        log.warn("handleMethodArgumentNotValid", e);
        return handleExceptionInternal(e, CommonErrorCode.INVALID_PARAMETER);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        log.warn("handleAllException", ex);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode,
        final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(makeErrorResponse(errorCode, message));
    }

    private ResponseEntity<Object> handleExceptionInternal(final BindException e,
        final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(final ErrorCode errorCode) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();
    }

    private ErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(message)
            .build();
    }

    private ErrorResponse makeErrorResponse(final BindException e, final ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(ErrorResponse.ValidationError::of)
            .collect(Collectors.toList());

        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .errors(validationErrorList)
            .build();
    }
}