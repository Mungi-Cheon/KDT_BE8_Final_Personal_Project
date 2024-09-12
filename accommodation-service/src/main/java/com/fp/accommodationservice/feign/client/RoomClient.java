package com.fp.accommodationservice.feign.client;

import com.fp.accommodationservice.feign.dto.response.room.RoomDetailResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "room-service", url = "${spring.cloud.openfeign.dest-room-url}")
public interface RoomClient {

    @GetMapping("/api/accommodation/{accommodationId}/room-details")
    List<RoomDetailResponse> getRoomDetailList(
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    );
}
