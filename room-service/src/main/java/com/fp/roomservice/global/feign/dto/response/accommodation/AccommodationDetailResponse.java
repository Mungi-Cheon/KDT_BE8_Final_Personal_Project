package com.fp.roomservice.global.feign.dto.response.accommodation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDetailResponse {

    private Long id;

    private String name;

    private String contact;

    private String description;

    private String address;

    private String category;

    private List<String> imageUrlList;
}