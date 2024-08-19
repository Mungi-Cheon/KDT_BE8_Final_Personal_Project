package com.fp.reviewservice.controller;

import com.fp.reviewservice.dto.response.AccommodationReviewResponseList;
import com.fp.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/review/{accommodationId}")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

//    @GetMapping
//    public ResponseEntity<AccommodationReviewResponseList> getReviewList(
//        @PathVariable Long accommodationId) {
//
//        return  ResponseEntity.ok(reviewService.getReviewList(accommodationId));
//    }
}
