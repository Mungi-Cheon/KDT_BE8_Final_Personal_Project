package com.fp.reservationservice.global.kafka.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.reservationservice.global.kafka.dto.ReservationMessage;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationProducer {

    private static final String RESERVATION_TOPIC = "room-reserve";
    private static final String CANCEL_TOPIC = "room-cancel";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReservation(final Long reservationId, final Long roomId,
        final LocalDate checkIn,
        final LocalDate checkOut) throws JsonProcessingException {
        final ReservationMessage message = new ReservationMessage(reservationId, roomId, checkIn,
            checkOut);
        kafkaTemplate.send(RESERVATION_TOPIC, objectMapper.writeValueAsString(message));
    }

    public void sendCancelReservation(final Long reservationId, final Long roomId,
        final LocalDate checkIn,
        final LocalDate checkOut) throws JsonProcessingException {
        final ReservationMessage message = new ReservationMessage(reservationId, roomId,
            checkIn,
            checkOut);
        kafkaTemplate.send(CANCEL_TOPIC, objectMapper.writeValueAsString(message));
    }
}
