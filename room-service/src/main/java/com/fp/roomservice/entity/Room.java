package com.fp.roomservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accommodationId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String checkInTime;

    private String checkOutTime;

    private int standardNumber;

    private int maximumNumber;

    private String type;

    private Long optionId;

    @ElementCollection
    private List<Long> infoPerNightsIds;

    @ElementCollection
    private List<Long> imageIds;
}
