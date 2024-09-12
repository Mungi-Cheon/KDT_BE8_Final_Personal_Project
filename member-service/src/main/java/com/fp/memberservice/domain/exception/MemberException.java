package com.fp.memberservice.domain.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class MemberException extends HttpStatusCodeException {

    public MemberException(ErrorType errorType) {
        super(errorType.getStatusCode(), errorType.getMessage());
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

