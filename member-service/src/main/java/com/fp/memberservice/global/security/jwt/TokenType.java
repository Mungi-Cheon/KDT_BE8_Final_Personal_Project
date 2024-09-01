package com.fp.memberservice.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {

    ACCESS("access-token"),
    REFRESH("refresh-token");

    private final String name;
}
