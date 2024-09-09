package com.fp.reservationservice.global.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record ReservationMessage(@JsonProperty("reservationId") Long reservationId,
                                 @JsonProperty("roomId") Long roomId,
                                 @JsonProperty("checkInDate") LocalDate checkInDate,
                                 @JsonProperty("checkOutDate") LocalDate checkOutDate) {

}
