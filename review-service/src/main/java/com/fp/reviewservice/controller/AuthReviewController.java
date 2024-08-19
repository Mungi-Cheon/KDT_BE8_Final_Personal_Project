package com.fp.reviewservice.controller;

import com.fp.reviewservice.dto.request.ReviewRequest;
import com.fp.reviewservice.dto.response.AccommodationReviewResponseList;
import com.fp.reviewservice.dto.response.DeleteReviewResponse;
import com.fp.reviewservice.dto.response.ReviewResponse;
import com.fp.reviewservice.dto.response.UpdateReviewResponse;
import com.fp.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth/review")
@RequiredArgsConstructor
public class AuthReviewController {

    private final ReviewService reviewService;

//    @PostMapping("/{accommodationId}")
//    public ResponseEntity<ReviewResponse> createReview(
//            @TokenMemberId Long tokenUserId,
//            @PathVariable Long accommodationId,
//            @RequestBody @Valid ReviewRequest reviewRequest) {
//
//        return ResponseEntity.ok(reviewService.createReview(tokenUserId, accommodationId,
//                reviewRequest));
//    }
//
//    @PutMapping("/{accommodationId}/{reviewId}")
//    public ResponseEntity<UpdateReviewResponse> updateReview(
//            @TokenMemberId Long tokenUserId,
//            @PathVariable Long accommodationId,
//            @PathVariable Long reviewId,
//            @RequestBody @Valid ReviewRequest reviewRequest) {
//        return ResponseEntity.ok(reviewService.updateReview(tokenUserId, accommodationId,
//                reviewRequest, reviewId));
//    }
//
//    @DeleteMapping("/{accommodationId}/{reviewId}")
//    public ResponseEntity<DeleteReviewResponse> deleteReview(
//            @TokenMemberId Long tokenUserId,
//            @PathVariable Long accommodationId,
//            @PathVariable Long reviewId) {
//
//        return ResponseEntity.ok(
//                reviewService.deleteReview(tokenUserId, accommodationId, reviewId));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ReviewResponse>> getMyReviewList(
//            @TokenMemberId Long tokenUserId) {
//        return ResponseEntity.ok(reviewService.getMyReviewList(tokenUserId));
//    }
//
//    @GetMapping("/{accommodationId}")
//    public ResponseEntity<AccommodationReviewResponseList> getReview(
//            @TokenMemberId Long tokenUserId,
//            @PathVariable Long accommodationId) {
//        return ResponseEntity.ok(reviewService.getReview(tokenUserId, accommodationId));
//    }

}
