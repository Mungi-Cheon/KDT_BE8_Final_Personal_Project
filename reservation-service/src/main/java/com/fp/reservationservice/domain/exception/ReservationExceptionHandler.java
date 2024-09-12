package com.fp.reservationservice.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "com.fp.reservationservice.domain")
@Order(value = 1)
public class ReservationExceptionHandler {

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> ReservationException(HttpStatusCodeException ex) {
        log.info("Exception type: {}, message: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

}
