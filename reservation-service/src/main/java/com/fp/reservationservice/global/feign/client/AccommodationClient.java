package com.fp.reservationservice.global.feign.client;

import com.fp.reservationservice.global.feign.dto.response.accommodation.AccommodationDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "accommodation-service", url = "${spring.cloud.openfeign.dest-accommodation-url}")
public interface AccommodationClient {

    @GetMapping("/api/accommodations/{accommodationId}")
    AccommodationDetailResponse getAccommodationDetail(@PathVariable Long accommodationId);

}
