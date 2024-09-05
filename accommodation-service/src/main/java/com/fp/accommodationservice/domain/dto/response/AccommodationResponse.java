package com.fp.accommodationservice.domain.dto.response;

import com.fp.accommodationservice.domain.entity.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationResponse {

    private Long id;

    private String name;

    private Integer price;

    private String thumbnail;

    private String category;


    public static AccommodationResponse from(Accommodation accommodation, String imageUrl,
        int price) {
        return AccommodationResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .price(price)
            .thumbnail(imageUrl)
            .category(accommodation.getCategory())
            .build();
    }
}