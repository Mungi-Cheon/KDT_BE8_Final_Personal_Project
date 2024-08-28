package com.fp.roomservice.response;


import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RoomListResponse {

    private Long accommodationId;

    private String accommodationName;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private List<RoomResponse> roomResponseList;

    private String category;

    public static RoomListResponse from(Long accommodationId,
        String accommodationName,
        List<RoomResponse> roomResponseList,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return RoomListResponse.builder()
            .accommodationId(accommodationId)
            .accommodationName(accommodationName)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .roomResponseList(roomResponseList)
            .build();
    }
}