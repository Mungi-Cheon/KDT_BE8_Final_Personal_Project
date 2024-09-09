package com.fp.roomservice.global.kafka.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.roomservice.domain.service.RoomService;
import com.fp.roomservice.global.kafka.dto.ReservationMessage;
import com.fp.roomservice.global.kafka.dto.ReservationMessageResponse;
import com.fp.roomservice.global.kafka.dto.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {

    private final RoomService roomService;
    private final TransactionProducer transactionProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "room-reserve", groupId = "reservation-group")
    public void consumeReservationEvent(final String reservationMessage)
        throws JsonProcessingException {
        final ReservationMessage message = objectMapper.readValue(reservationMessage,
            ReservationMessage.class);

        try {
            roomService.decreaseCountByOne(message.roomId(), message.checkInDate(),
                message.checkOutDate());
            final ReservationMessageResponse response = new ReservationMessageResponse(
                message.reservationId(), ReservationStatus.SUCCESS,
                StringUtils.EMPTY);
            transactionProducer.sendTransactionResult(response);

        } catch (Exception e) {
            final ReservationMessageResponse response = new ReservationMessageResponse(
                message.reservationId(), ReservationStatus.FAILURE,
                e.getMessage());
            transactionProducer.sendTransactionResult(response);
        }
    }

    @KafkaListener(topics = "room-cancel", groupId = "reservation-group")
    public void consumeReservationCancelEvent(final String cancelMessage)
        throws JsonProcessingException {
        final ReservationMessage message = objectMapper.readValue(cancelMessage,
            ReservationMessage.class);

        roomService.increaseCountByOne(message.roomId(), message.checkInDate(),
            message.checkInDate());
        final ReservationMessageResponse response = new ReservationMessageResponse(
            message.reservationId(), ReservationStatus.CANCEL, StringUtils.EMPTY);
        transactionProducer.sendTransactionResult(response);
    }
}
