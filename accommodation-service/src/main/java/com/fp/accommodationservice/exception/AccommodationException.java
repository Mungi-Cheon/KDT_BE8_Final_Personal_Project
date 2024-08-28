package com.fp.accommodationservice.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    public AccommodationException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}