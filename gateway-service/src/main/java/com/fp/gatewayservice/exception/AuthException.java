package com.fp.gatewayservice.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class AuthException extends HttpStatusCodeException {

    public AuthException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

