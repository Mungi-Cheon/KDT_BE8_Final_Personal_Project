package com.fp.roomservice.global.kafka.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.roomservice.global.kafka.dto.ReservationMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProducer {

    private static final String TOPIC_RESERVATION_TRANSACTION_RESULT = "reservation-transaction-result";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendTransactionResult(final ReservationMessageResponse responseMessage)
        throws JsonProcessingException {
        final String message = objectMapper.writeValueAsString(responseMessage);
        kafkaTemplate.send(TOPIC_RESERVATION_TRANSACTION_RESULT, message);
    }

}
