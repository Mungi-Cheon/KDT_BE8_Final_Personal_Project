package com.fp.reservationservice.global.feign.dto.response.room;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomImageResponse {

    private List<String> imageUrlList;
}