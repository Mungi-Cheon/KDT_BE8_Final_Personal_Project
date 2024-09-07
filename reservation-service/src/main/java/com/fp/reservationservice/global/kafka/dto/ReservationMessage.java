package com.fp.reservationservice.global.kafka.dto;

import java.time.LocalDate;

public record ReservationMessage(Long reservationId, Long roomId, LocalDate checkInDate,
                                 LocalDate checkOutDate) {

}
