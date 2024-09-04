package com.fp.gatewayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {
    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않음"),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지않습니다."),
    TOKEN_AUTHORIZATION_FAIL(HttpStatus.FORBIDDEN, "토큰 인증 실패"),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다.");

    private final HttpStatusCode statusCode;
    private final String message;
}
