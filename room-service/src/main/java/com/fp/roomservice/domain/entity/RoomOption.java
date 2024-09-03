package com.fp.roomservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long roomId;

    private Boolean hasBath;

    private Boolean hasAirCondition;

    private Boolean hasTv;

    private Boolean hasPc;

    private Boolean hasWifi;

    private Boolean hasCable;

    private Boolean hasRefrigerator;

    private Boolean hasSofa;

    private Boolean canCook;

    private Boolean hasTable;

    private Boolean hasHairdryer;
}