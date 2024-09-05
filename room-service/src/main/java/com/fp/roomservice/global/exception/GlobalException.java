package com.fp.roomservice.global.exception;

import com.fp.roomservice.global.exception.type.RedisErrorType;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class GlobalException extends HttpStatusCodeException {

    public GlobalException(RedisErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    public GlobalException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}
