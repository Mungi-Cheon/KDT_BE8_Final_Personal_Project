package com.fp.reservationservice.global.feign.client;

import com.fp.reservationservice.global.feign.dto.response.room.RoomDetailResponse;
import com.fp.reservationservice.global.feign.dto.response.room.RoomImageResponse;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "room-service", url = "${spring.cloud.openfeign.dest-room-url}")
public interface RoomClient {


    @GetMapping("/api/accommodation/{accommodationId}/rooms/{roomId}")
    RoomDetailResponse getRoomDetail(@PathVariable Long accommodationId,
        @PathVariable Long roomId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") Integer personNumber);

    @GetMapping("/api/accommodation/{accommodationId}/rooms/{roomId}/images")
    RoomImageResponse getRoomImagesByRoomIds(
        @PathVariable Long accommodationId, @PathVariable Long roomId);
}
