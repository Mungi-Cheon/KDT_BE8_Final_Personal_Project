package com.fp.reservationservice.domain.dto.response;

import com.fp.reservationservice.domain.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReservationHistoryResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer night;

    private Integer personNumber;

    private Long accommodationId;

    private String accommodationName;

    private String roomType;

    private Integer standardNumber;

    private Integer maximumNumber;

    private List<String> imageUrlList;

    private Integer price;

    private Integer totalPrice;

    public static ReservationHistoryResponse from(Reservation entity, Long accommodationId,
        String accommodationName, String roomType, Integer standardNumber,
        Integer maximumNumber, List<String> imageUrlList) {
        return ReservationHistoryResponse.builder()
            .id(entity.getId())
            .checkInDate(entity.getCheckInDate())
            .checkOutDate(entity.getCheckOutDate())
            .night(entity.getNight())
            .personNumber(entity.getPersonNumber())
            .accommodationId(accommodationId)
            .accommodationName(accommodationName)
            .roomType(roomType)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .imageUrlList(imageUrlList)
            .price(entity.getPrice())
            .totalPrice(calcTotalPrice(entity.getPrice(), entity.getNight()))
            .build();
    }

    private static int calcTotalPrice(int price, int night) {
        return price * night;
    }
}
