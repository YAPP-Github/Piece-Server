package org.yapp.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.yapp.error.dto.ErrorCode;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
}
