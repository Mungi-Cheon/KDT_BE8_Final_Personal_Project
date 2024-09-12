package com.fp.reservationservice.global.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ReservationMessageResponse(@JsonProperty("reservationId") Long reservationId,
                                         @JsonProperty("status") ReservationStatus status,
                                         @JsonProperty("message") String message) {

}
