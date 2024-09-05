package com.fp.accommodationservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND, "조회 결과가 없습니다."),
    INVALID_CHECK_IN(HttpStatus.BAD_REQUEST, "체크인 날짜는 오늘 또는 이후 날짜여야 합니다."),
    INVALID_CHECK_OUT(HttpStatus.BAD_REQUEST, "체크아웃 날짜는 체크인 날짜 이후여야 합니다."),
    INVALID_NUMBER_OF_PEOPLE(HttpStatus.BAD_REQUEST, "잘못된 인원");

    private final HttpStatusCode statusCode;
    private final String message;
}
