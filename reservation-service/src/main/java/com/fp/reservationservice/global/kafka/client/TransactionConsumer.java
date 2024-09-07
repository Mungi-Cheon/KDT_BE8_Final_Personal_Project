package com.fp.reservationservice.global.kafka.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.reservationservice.domain.repository.ReservationRepository;
import com.fp.reservationservice.global.kafka.dto.ReservationMessageResponse;
import com.fp.reservationservice.global.kafka.dto.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final ReservationRepository reservationRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "reservation-transaction-result", groupId = "reservation-transaction-result-group")
    public void consumeReservationTransactionResultEvent(final String roomResponseMessage) {
        final ReservationMessageResponse response = objectMapper.convertValue(roomResponseMessage,
            ReservationMessageResponse.class);

        if (response.status() == ReservationStatus.FAILURE) {
            reservationRepository.updateStatusToFailureById(response.reservationId());
            log.info("Reservation transaction failed with Id And Message. ID : {}, MESSAGE {}",
                response.reservationId(), response.message());
            return;
        }
        reservationRepository.updateStatusToSuccessById(response.reservationId());
    }
}


