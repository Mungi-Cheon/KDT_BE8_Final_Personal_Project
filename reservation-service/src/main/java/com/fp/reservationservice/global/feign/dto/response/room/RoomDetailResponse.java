package com.fp.reservationservice.global.feign.dto.response.room;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    private List<String> roomImageList;

    private RoomOptionResponse productOption;
}