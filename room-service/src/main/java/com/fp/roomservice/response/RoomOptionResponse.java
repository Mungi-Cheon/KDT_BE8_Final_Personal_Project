package com.fp.roomservice.response;

import com.fp.roomservice.entity.RoomOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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

    public static RoomOptionResponse from(RoomOption roomOption) {
        return RoomOptionResponse.builder()
            .hasBath(roomOption.getHasBath())
            .hasAirCondition(roomOption.getHasAirCondition())
            .hasTv(roomOption.getHasTv())
            .hasPc(roomOption.getHasPc())
            .hasWifi(roomOption.getHasWifi())
            .hasCable(roomOption.getHasCable())
            .hasRefrigerator(roomOption.getHasRefrigerator())
            .hasSofa(roomOption.getHasSofa())
            .canCook(roomOption.getCanCook())
            .hasTable(roomOption.getHasTable())
            .hasHairdryer(roomOption.getHasHairdryer())
            .build();
    }
}