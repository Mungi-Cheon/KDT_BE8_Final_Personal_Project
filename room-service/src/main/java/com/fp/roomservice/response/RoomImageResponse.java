package com.fp.roomservice.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomImageResponse {

    private List<String> imageUrlList;

    public static RoomImageResponse from(List<String> imageUrlList) {
        return RoomImageResponse.builder()
            .imageUrlList(imageUrlList)
            .build();
    }
}