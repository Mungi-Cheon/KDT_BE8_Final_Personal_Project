package com.fp.reservationservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {

    NOT_FOUND(HttpStatus.NOT_FOUND, "조회 결과가 없습니다."),
    INCLUDES_FULLY_BOOKED_ROOM(HttpStatus.BAD_REQUEST, "예약 마감된 객실이 포함되어 있습니다."),
    ALREADY_RESERVATION(HttpStatus.CONFLICT, "이미 예약된 숙박 정보가 존재합니다.");

    private final HttpStatusCode statusCode;
    private final String message;
}
