package org.yapp.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse<T> {
  private static final String SUCCESS_STATUS = "success";
  private static final String FAIL_STATUS = "fail";
  private static final String ERROR_STATUS = "error";

  private String status;
  private String message;
  private T data;

  private CommonResponse(String status, T data, String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public static CommonResponse<?> createError(String message) {
    return new CommonResponse<>(ERROR_STATUS, null, message);
  }

  public static CommonResponse<?> createFail(BindingResult bindingResult) {
    Map<String, String> errors = new HashMap<>();

    List<ObjectError> allErrors = bindingResult.getAllErrors();
    for (ObjectError error : allErrors) {
      if (error instanceof FieldError) {
        errors.put(((FieldError) error).getField(), error.getDefaultMessage());
      } else {
        errors.put(error.getObjectName(), error.getDefaultMessage());
      }
    }
    return new CommonResponse<>(FAIL_STATUS, errors, null);
  }

  public static <T> CommonResponse<T> createSuccess(T data) {
    return new CommonResponse<>(SUCCESS_STATUS, data, "요청이 성공적으로 처리되었습니다.");
  }

  public static CommonResponse<Void> createSuccessWithNoContent() {
    return new CommonResponse<>(SUCCESS_STATUS, null, null);
  }
}