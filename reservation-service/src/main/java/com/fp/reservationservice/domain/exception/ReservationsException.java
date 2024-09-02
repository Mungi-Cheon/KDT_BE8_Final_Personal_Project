package com.fp.reservationservice.domain.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class ReservationsException extends HttpStatusCodeException {

    public ReservationsException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

