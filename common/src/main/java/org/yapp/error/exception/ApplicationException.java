package org.yapp.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.yapp.error.dto.CommonErrorCode;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {
    private final CommonErrorCode errorCode;
}
