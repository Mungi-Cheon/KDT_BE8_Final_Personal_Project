package com.fp.memberservice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
@Getter
public enum ErrorType {

    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않음"),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지않습니다."),
    TOKEN_AUTHORIZATION_FAIL(HttpStatus.FORBIDDEN, "토큰 인증 실패"),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    AUTHORIZATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "인증할 수 없는 오류가 발생. 로그 확인이 필요합니다.");

    private final HttpStatusCode statusCode;
    private final String message;
}
