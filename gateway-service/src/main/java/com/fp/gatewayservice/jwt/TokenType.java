package com.fp.gatewayservice.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {

    ACCESS("access-token"),
    REFRESH("refresh-token");

    private final String name;
}
