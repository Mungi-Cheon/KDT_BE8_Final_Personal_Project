package com.fp.roomservice.controller;

import com.fp.roomservice.response.RoomDetailResponse;
import com.fp.roomservice.response.RoomListResponse;
import com.fp.roomservice.response.RoomSimpleResponse;
import com.fp.roomservice.service.RoomService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accommodations/{accommodationId}")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<RoomListResponse> getAccommodationDetail(
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        RoomListResponse response = roomService
            .getRoomList(accommodationId, checkInDate, checkOutDate, personNumber);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<RoomDetailResponse> getProductDetail(
        @PathVariable Long accommodationId,
        @PathVariable Long productId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") Integer personNumber
    ) {

        var response = roomService.getRoomDetail(accommodationId, productId,
            checkInDate, checkOutDate, personNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomSimpleResponse>> getSearchProduct(
        @PathVariable Long accommodationId,
        @RequestParam String keyword
    ) {
        var response = roomService.getSearchRoom(accommodationId, keyword);
        return ResponseEntity.ok(response);
    }

}