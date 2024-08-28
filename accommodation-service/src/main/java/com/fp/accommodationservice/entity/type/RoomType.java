package com.fp.accommodationservice.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomType {

    STANDARD("standard"),
    VIP("vip");

    private final String name;
}
