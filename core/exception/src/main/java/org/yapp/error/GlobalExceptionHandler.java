package org.yapp.error;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.yapp.error.dto.CommonErrorCode;
import org.yapp.error.dto.ErrorCode;
import org.yapp.error.dto.ErrorResponse;
import org.yapp.error.exception.ApplicationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleQuizException(final ApplicationException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        final MethodArgumentNotValidException e,
        final HttpHeaders headers,
        final HttpStatusCode status,
        final WebRequest request) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        log.warn("handleAllException", ex);
        final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
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
        final List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
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