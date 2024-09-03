package com.fp.roomservice.domain.dto.response;


import com.fp.roomservice.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RoomDetailResponse {

    private Long id;

    private String name;

    private String accommodationName;

    private String description;

    private int totalPrice;

    private int price;

    private int numberOfStay;

    private int standardNumber;

    private int maximumNumber;

    private String type;

    private RoomImageResponse roomImageResponse;

    private RoomOptionResponse productOption;


    public static RoomDetailResponse from(Room room, String accommodationName,
        int price, int totalPrice,
        int numberOfStay, RoomImageResponse roomImageResponse,
        RoomOptionResponse roomOptionResponse) {
        return RoomDetailResponse.builder()
            .id(room.getId())
            .name(room.getName())
            .accommodationName(accommodationName)
            .description(room.getDescription())
            .price(price)
            .totalPrice(totalPrice)
            .numberOfStay(numberOfStay)
            .standardNumber(room.getStandardNumber())
            .maximumNumber(room.getMaximumNumber())
            .type(room.getType())
            .roomImageResponse(roomImageResponse)
            .productOption(roomOptionResponse)
            .build();
    }
}