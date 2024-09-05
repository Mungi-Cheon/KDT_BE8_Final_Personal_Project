package com.fp.roomservice.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@Slf4j
@RestControllerAdvice(basePackages = "com.fp.roomservice.domain")
@Order(value = 1)
public class RoomExceptionHandler {

    private final String LOG_TYPE_WARN = "WARN";
    private final String LOG_TYPE_INFO = "INFO";

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> globalException(Exception ex) {
        writeLog(LOG_TYPE_WARN, ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(value = {HttpStatusCodeException.class})
    public ResponseEntity<?> AccommodationException(HttpStatusCodeException ex) {
        writeLog(LOG_TYPE_INFO, ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    private void writeLog(String logType, String type, String message) {
        switch (logType) {
            case LOG_TYPE_WARN:
                log.warn("Exception type: {}, message: {}", type, message);
                break;
            case LOG_TYPE_INFO:
                log.info("Exception type: {}, message: {}", type, message);
                break;
            default:
                log.debug("Exception type: {}, message: {}", type, message);
        }
    }
}
