package com.fp.roomservice.response;


import com.fp.roomservice.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RoomSimpleResponse {

    private Long id;

    private String name;

    private String checkInTime;

    private String checkOutTime;

    private int standardNumber;

    private int maximumNumber;

    private RoomImageResponse imageResponse;

    public static RoomSimpleResponse from(Room room, RoomImageResponse imageResponse) {
        return RoomSimpleResponse.builder()
            .id(room.getId())
            .name(room.getName())
            .checkInTime(room.getCheckInTime())
            .checkOutTime(room.getCheckOutTime())
            .standardNumber(room.getStandardNumber())
            .maximumNumber(room.getMaximumNumber())
            .imageResponse(imageResponse)
            .build();
    }
}
