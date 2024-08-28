package com.fp.accommodationservice.controller;

import com.fp.accommodationservice.dto.request.AccommodationRequest;
import com.fp.accommodationservice.dto.response.AccommodationResponse;
import com.fp.accommodationservice.entity.type.Category;
import com.fp.accommodationservice.service.AccommodationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
