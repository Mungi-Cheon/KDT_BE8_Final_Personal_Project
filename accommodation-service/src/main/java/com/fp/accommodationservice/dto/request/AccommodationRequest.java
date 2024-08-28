package com.fp.accommodationservice.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccommodationRequest {

    private Integer categoryId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer personNumber;
}
