package com.fp.accommodationservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccommodationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean hasSmokingRoom;

    private Boolean hasCooking;

    private Boolean hasParking;

    private Boolean hasSwimmingPool;

    private Boolean hasBreakfast;

    private Boolean hasFitness;

    private Boolean hasBeauty;
}
