package com.fp.reservationservice.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long accommodationId;

    private Long roomId;

    private String accommodationName;

    private Integer personNumber;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer price;

    private Integer night;

    private String roomType;

    private Integer standardNumber;

    private Integer maximumNumber;
}
