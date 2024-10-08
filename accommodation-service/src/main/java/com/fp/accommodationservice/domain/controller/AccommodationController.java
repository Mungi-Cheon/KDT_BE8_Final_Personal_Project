package com.fp.accommodationservice.domain.controller;

import com.fp.accommodationservice.domain.dto.request.AccommodationRequest;
import com.fp.accommodationservice.domain.dto.response.AccommodationResponse;
import com.fp.accommodationservice.domain.entity.type.Category;
import com.fp.accommodationservice.domain.dto.response.AccommodationDetailResponse;
import com.fp.accommodationservice.domain.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<List<AccommodationResponse>> getAvailableAccommodations(
        @ModelAttribute AccommodationRequest request,
        @RequestParam(required = false) Long lastAccommodationId) {

        String category = Category.fromId(request.getCategoryId());

        int personNumber = request.getPersonNumber() == null ? 2 : request.getPersonNumber();

        List<AccommodationResponse> responses = accommodationService
            .getAvailableAccommodations(category, request.getCheckInDate(),
                request.getCheckOutDate(), personNumber, lastAccommodationId);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<AccommodationDetailResponse> getAccommodationDetail(
        @PathVariable Long accommodationId) {
        AccommodationDetailResponse response = accommodationService.findAccommodation(
            accommodationId);
        return ResponseEntity.ok(response);
    }
}
