package com.fp.roomservice.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum RedisErrorType {

    KEY_NOT_GAIN(HttpStatus.NOT_FOUND, "키에 대한 락을 획득할 수 없습니다"),
    KEY_INTERRUPTED(HttpStatus.CONFLICT, "키 획득 중 인터럽트 발생했습니다"),
    QUEUE_ERROR(HttpStatus.CONFLICT, "큐에 대한 에러가 발생했습니다"),
    QUEUE_DATA_NOT_ADDED(HttpStatus.CONFLICT, "큐에 데이터가 저장되는 부분에서 에러가 발생했습니다"),
    REDIS_ERROR(HttpStatus.CONFLICT, "Redis에서 에러가 발생했습니다");

    private final HttpStatusCode statusCode;
    private final String message;
}
