package com.fp.roomservice.domain.exception;

import com.fp.roomservice.global.exception.type.RoomErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class RoomException extends HttpStatusCodeException {

    public RoomException(RoomErrorType roomErrorType) {
        super(roomErrorType.getStatusCode(), roomErrorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}