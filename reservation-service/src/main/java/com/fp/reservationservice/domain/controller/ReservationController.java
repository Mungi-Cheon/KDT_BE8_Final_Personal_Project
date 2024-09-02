package com.fp.reservationservice.domain.controller;

import com.fp.reservationservice.domain.dto.request.ReservationCancelRequest;
import com.fp.reservationservice.domain.dto.request.ReservationRequest;
import com.fp.reservationservice.domain.dto.response.ReservationCancelResponse;
import com.fp.reservationservice.domain.service.ReservationService;
import com.fp.reservationservice.domain.dto.response.ReservationHistoryListResponse;
import com.fp.reservationservice.domain.dto.response.ReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservation/history")
    public ResponseEntity<ReservationHistoryListResponse> getReservationHistories(
        @RequestHeader("member-id") Long memberId) {
        ReservationHistoryListResponse response = reservationService
            .getReservationHistories(memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> reservation(
        @RequestHeader("member-id") Long memberId,
        @Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService
            .reserve(request, memberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservation")
    public ResponseEntity<ReservationCancelResponse> reservationCancel(
        @RequestHeader("member-id") Long memberId, @RequestBody ReservationCancelRequest request) {
        ReservationCancelResponse response = reservationService
            .cancelReservation(memberId, request);
        return ResponseEntity.ok(response);
    }
}