package com.fp.accommodationservice.feign.dto.response.room;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomImageResponse {

    private List<String> imageUrlList;
}