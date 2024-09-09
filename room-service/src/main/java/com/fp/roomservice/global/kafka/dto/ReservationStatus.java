package com.fp.roomservice.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {

    PENDING("예약 대기"),
    SUCCESS("예약 성공"),
    CANCEL("예약 취소"),
    FAILURE("예약 실패");

    private final String message;
}
