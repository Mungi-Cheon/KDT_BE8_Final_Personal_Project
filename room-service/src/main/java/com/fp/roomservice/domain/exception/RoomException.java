package com.fp.roomservice.domain.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class RoomException extends HttpStatusCodeException {

    public RoomException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}