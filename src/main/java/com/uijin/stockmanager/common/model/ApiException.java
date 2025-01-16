package com.uijin.stockmanager.common.model;

import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{
  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  ApiException(HttpStatus httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  public static ApiException from(ApiExceptionCode apiExceptionCode) {
    return new ApiException(
            apiExceptionCode.getHttpStatus(),
            apiExceptionCode.getCode(),
            apiExceptionCode.getMessage()
    );
  }
}
