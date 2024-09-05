package com.fp.accommodationservice.feign.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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