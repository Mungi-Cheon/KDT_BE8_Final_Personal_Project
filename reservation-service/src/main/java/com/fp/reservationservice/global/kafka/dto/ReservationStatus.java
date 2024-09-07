package com.fp.reservationservice.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {

    PENDING("예약 대기"),
    SUCCESS("예약 성공"),
    FAILURE("예약 실패");

    private final String message;
}
