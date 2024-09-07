package com.fp.reservationservice.global.kafka.dto;

public record ReservationMessageResponse(Long reservationId, ReservationStatus status,
                                         String message) {

}
