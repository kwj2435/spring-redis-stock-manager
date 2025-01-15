package com.uijin.stockmanager.common.exception;

import com.uijin.stockmanager.common.model.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
    return ResponseEntity.internalServerError().body(new ApiErrorResponse());
  }

}
