package com.fp.roomservice.domain.dto.response;


import com.fp.roomservice.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RoomResponse {

    private Long id;

    private String name;

    private String checkInTime;

    private String checkOutTime;

    private int price;

    private int standardNumber;

    private int maximumNumber;

    private RoomImageResponse images;

    private int count;

    public static RoomResponse from(
        Room room, int count, int price,
        RoomImageResponse roomImageResponse) {
        return RoomResponse.builder()
            .id(room.getId())
            .name(room.getName())
            .checkInTime(room.getCheckInTime())
            .checkOutTime(room.getCheckOutTime())
            .price(price)
            .standardNumber(room.getStandardNumber())
            .maximumNumber(room.getMaximumNumber())
            .images(roomImageResponse)
            .count(count)
            .build();
    }
}
