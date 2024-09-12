package com.fp.memberservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND, "조회 결과가 없습니다."),
    INVALID_EMAIL_AND_PASSWORD(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다.");

    private final HttpStatusCode statusCode;
    private final String message;
}
