package com.uijin.stockmanager.common.model;

import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import lombok.Getter;

@Getter
public class ApiErrorResponse {
  private final String code;
  private final String message;

  public ApiErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public static ApiErrorResponse from(ApiException apiException) {
    return new ApiErrorResponse(apiException.getCode(), apiException.getMessage());
  }

  public static ApiErrorResponse internalServerError() {
    return new ApiErrorResponse(
            ApiExceptionCode.ERR_400_10001.getCode(),
            ApiExceptionCode.ERR_500_10001.getMessage()
    );
  }
}
