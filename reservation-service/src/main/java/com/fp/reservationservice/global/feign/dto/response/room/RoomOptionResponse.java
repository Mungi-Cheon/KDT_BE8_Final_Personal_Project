package com.fp.reservationservice.global.feign.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomOptionResponse {

    private boolean hasBath;

    private boolean hasAirCondition;

    private boolean hasTv;

    private boolean hasPc;

    private boolean hasWifi;

    private boolean hasCable;

    private boolean hasRefrigerator;

    private boolean hasSofa;

    private boolean canCook;

    private boolean hasTable;

    private boolean hasHairdryer;
}