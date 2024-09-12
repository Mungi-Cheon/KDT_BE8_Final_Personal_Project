package com.fp.roomservice.global.exception;

import com.fp.roomservice.global.exception.type.RedisErrorType;
import org.springframework.web.client.HttpStatusCodeException;

public class GlobalException extends HttpStatusCodeException {

    public GlobalException(RedisErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
