package com.fp.roomservice.domain.dto.response;


import com.fp.roomservice.domain.entity.Room;
import java.util.List;
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

    private int roomCount;

    private List<String> roomImageUrlList;

    private RoomOptionResponse productOption;


    public static RoomDetailResponse from(Room room, String accommodationName,
        int price, int totalPrice,
        int numberOfStay, int roomCount,
        List<String> roomImageUrlList, RoomOptionResponse roomOptionResponse) {
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
            .roomCount(roomCount)
            .type(room.getType())
            .roomImageUrlList(roomImageUrlList)
            .productOption(roomOptionResponse)
            .build();
    }
}