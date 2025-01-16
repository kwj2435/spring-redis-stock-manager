package com.uijin.stockmanager.common.exception;

import com.uijin.stockmanager.common.enums.ApiExceptionCode;
import com.uijin.stockmanager.common.model.ApiErrorResponse;
import com.uijin.stockmanager.common.model.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
    log.error("An unexpected error occurred", exception);
    ApiErrorResponse response = ApiErrorResponse.internalServerError();

    return new ResponseEntity<>(response, ApiExceptionCode.ERR_500_10001.getHttpStatus());
  }

  @ExceptionHandler(value = ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleApiException(ApiException apiException) {
    log.debug(apiException.getMessage());
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.from(apiException);

    return new ResponseEntity<>(apiErrorResponse, apiException.getHttpStatus());
  }
}
