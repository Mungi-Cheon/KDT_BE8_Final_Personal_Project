package com.fp.roomservice.domain.controller;

import com.fp.roomservice.domain.dto.response.RoomDetailResponse;
import com.fp.roomservice.domain.dto.response.RoomImageResponse;
import com.fp.roomservice.domain.dto.response.RoomListResponse;
import com.fp.roomservice.domain.dto.response.RoomSimpleResponse;
import com.fp.roomservice.domain.service.RoomService;
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
@RequestMapping("/api/accommodations/{accommodationId}/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<RoomListResponse> getRoomList(
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        RoomListResponse response = roomService
            .getRoomList(accommodationId, checkInDate, checkOutDate, personNumber);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponse> getRoomDetail(
        @PathVariable Long accommodationId,
        @PathVariable Long roomId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") Integer personNumber
    ) {

        RoomDetailResponse response = roomService.getRoomDetail(accommodationId, roomId,
            checkInDate, checkOutDate, personNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/search")
    public ResponseEntity<List<RoomSimpleResponse>> getSearchRoom(
        @PathVariable Long accommodationId,
        @RequestParam String keyword
    ) {
        List<RoomSimpleResponse> response = roomService.getSearchRoom(accommodationId, keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/images")
    public ResponseEntity<RoomImageResponse> getRoomImagesByRoomIds(
        @PathVariable Long accommodationId, @PathVariable Long roomId) {
        RoomImageResponse response = roomService.findRoomImagesByRoomId(accommodationId, roomId);
        return ResponseEntity.ok(response);
    }

}