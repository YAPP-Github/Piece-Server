package org.yapp.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.yapp.core.exception.error.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
}
