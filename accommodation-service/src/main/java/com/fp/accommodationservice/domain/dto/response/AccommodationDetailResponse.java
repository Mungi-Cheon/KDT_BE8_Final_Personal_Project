package com.fp.accommodationservice.domain.dto.response;

import com.fp.accommodationservice.domain.entity.Accommodation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
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

    public static AccommodationDetailResponse from(Accommodation accommodation,
        List<String> imageUrlList) {
        return AccommodationDetailResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .contact(accommodation.getContact())
            .description(accommodation.getDescription())
            .address(accommodation.getAddress())
            .category(accommodation.getCategory())
            .imageUrlList(imageUrlList)
            .build();
    }
}