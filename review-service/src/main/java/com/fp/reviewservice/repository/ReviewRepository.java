package com.fp.reviewservice.repository;

import com.fp.reviewservice.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndAccommodationId(Long reviewId, Long accommodationId);

    boolean existsByReservationId(Long reservationId);

    List<Review> findByAccommodationId(Long accommodationId);

    default List<Review> getByAccommodationId(Long accommodationId) {
        return findByAccommodationId(accommodationId);
    }

//    default Review getByIdAndAccommodationId(Long reviewId, Long accommodationId) {
//        return findByIdAndAccommodationId(reviewId, accommodationId)
//            .orElseThrow(() -> new ReviewException(ErrorType.NOT_FOUND));
//    }
}