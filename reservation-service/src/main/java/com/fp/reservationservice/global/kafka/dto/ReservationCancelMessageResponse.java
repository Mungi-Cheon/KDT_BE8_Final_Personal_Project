package com.fp.reservationservice.global.kafka.dto;

public record ReservationCancelMessageResponse(Long reservationId, ReservationStatus status) {

}
