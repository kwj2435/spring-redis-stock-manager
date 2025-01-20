package com.uijin.stockmanager.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiExceptionCode {
  ERR_400_10001(HttpStatus.BAD_REQUEST, "400_10001", "잘못된 요청 입니다."),

  ERR_409_10001(HttpStatus.BAD_REQUEST, "409_10001", "요청하신 상품의 재고가 부족 합니다."),
  ERR_409_10002(HttpStatus.BAD_REQUEST, "409_10002", "재고 처리가 진행중입니다. 잠시후 다시 확인 해주세요"),

  ERR_500_10001(HttpStatus.INTERNAL_SERVER_ERROR, "500_10001", "개발팀에 문의 해주세요.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  ApiExceptionCode(HttpStatus httpStatus, String code, String message) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }
}
