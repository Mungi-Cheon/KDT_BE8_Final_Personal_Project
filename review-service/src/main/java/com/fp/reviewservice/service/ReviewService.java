package com.fp.reviewservice.service;

import com.fp.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
//    private final AccommodationRepository accommodationRepository;
//    private final MemberRepository memberRepository;
//    private final ReservationRepository reservationRepository;
//    private final ProductRepository productRepository;
//
//    @Transactional(readOnly = true)
//    public AccommodationReviewResponseList getReviewList(Long accommodationId) {
//
//        Accommodation accommodation = findAccommodation(accommodationId);
//        List<Review> reviewList = accommodation.getReviews();
//
//        List<ReviewResponse> reviewResponseList = reviewList.stream()
//            .map(ReviewResponse::from)
//            .collect(Collectors.toList());
//
//        return AccommodationReviewResponseList.from(
//            accommodation.getImages().getThumbnail(),
//            accommodation.getId(),
//            accommodation.getName(),
//            accommodation.getGrade(),
//            reviewResponseList);
//    }
//
//    @Transactional(readOnly = true)
//    public AccommodationReviewResponseList getReview(Long memberId, Long accommodationId) {
//        Member member = findMember(memberId);
//        Accommodation accommodation = findAccommodation(accommodationId);
//        Review review = member.getReviews().stream()
//            .filter(r -> accommodation.getId().equals(r.getAccommodation().getId()))
//            .findAny()
//            .orElse(null);
//
//        List<ReviewResponse> reviewResponseList = Stream.of(review)
//            .filter(Objects::nonNull)
//            .map(ReviewResponse::from)
//            .toList();
//
//        return AccommodationReviewResponseList.from(
//            accommodation.getImages().getThumbnail(),
//            accommodation.getId(),
//            accommodation.getName(),
//            accommodation.getGrade(), reviewResponseList);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ReviewResponse> getMyReviewList(Long memberId) {
//        Member member = findMember(memberId);
//        List<Review> reviewList = member.getReviews();
//
//        return reviewList.stream()
//            .map(ReviewResponse::from)
//            .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public ReviewResponse createReview(Long memberId, Long accommodationId,
//        ReviewRequest reviewRequest) {
//        Accommodation accommodation = findAccommodation(accommodationId);
//        Member member = findMember(memberId);
//        Reservation reservation = findReservation(reviewRequest.getReservationId());
//        Long productId = reservation.getProduct().getId();
//
//        productRepository.findByIdAndAccommodationId(productId, accommodationId)
//            .orElseThrow(() -> new ProductException(ErrorType.NOT_FOUND));
//
//        //중복 리뷰 검증
//        if (reviewRepository.existsByReservationId(reservation.getId())) {
//            throw new ReviewException(ErrorType.DUPLICATED_REVIEW);
//        }
//        //체크아웃 시간, 작성 시간 검증
////        LocalDate checkOutTime = reservation.getCheckOutDate();
////        LocalDate createdAt = LocalDate.now();
////        validInputs(checkOutTime, createdAt);
//
//        Review review = Review.from(reviewRequest, accommodation, member, reservation);
//        Review saved = reviewRepository.save(review);
//        return ReviewResponse.from(saved);
//    }
//
//    @Transactional
//    public UpdateReviewResponse updateReview(Long memberId, Long accommodationId,
//        ReviewRequest reviewRequest, Long reviewId) {
//        findAccommodation(accommodationId);
//        findMember(memberId);
//        Reservation reservation = findReservation(reviewRequest.getReservationId());
//        Review review = findByIdAndAccommodationId(reviewId, accommodationId);
//        LocalDate checkOutTime = reservation.getCheckOutDate();
//        LocalDate updatedAt = LocalDate.now();
//        isValidWrite(checkOutTime, updatedAt);
//        review.update(reviewRequest);
//
//        return UpdateReviewResponse.from(review);
//    }
//
//    @Transactional
//    public DeleteReviewResponse deleteReview(Long memberId,
//        Long accommodationId, Long reviewId) {
//        findMember(memberId);
//        Review review = findByIdAndAccommodationId(reviewId, accommodationId);
//        reviewRepository.delete(review);
//
//        return DeleteReviewResponse.from(review);
//    }
//
//    @Transactional
//    public void updateGrade() {
//        accommodationRepository.findAll().stream()
//            .peek(accommodation -> {
//                List<Review> reviews = reviewRepository
//                    .getByAccommodationId(accommodation.getId());
//                BigDecimal newGrade = calculateGrade(reviews, accommodation);
//                accommodation.updateGrade(newGrade);
//            })
//            .forEach(accommodationRepository::save);
//    }
//
//    private Accommodation findAccommodation(Long accommodationId) {
//        return accommodationRepository.findAccommodationById(accommodationId);
//    }
//
//    private Member findMember(Long memberId) {
//        return memberRepository.getMember(memberId);
//    }
//
//    private Reservation findReservation(Long reservationId) {
//        return reservationRepository.getReservationById(reservationId);
//    }
//
//    private Review findByIdAndAccommodationId(Long reviewId, Long accommodationId) {
//        return reviewRepository.getByIdAndAccommodationId(reviewId, accommodationId);
//    }
//
//    private static boolean isValidWrite(LocalDate checkOutDate, LocalDate createdAt) {
//        return createdAt.isAfter(checkOutDate) || createdAt.isEqual(checkOutDate);
//    }
//
//    private void validInputs(LocalDate checkOutDate, LocalDate createdAt) {
//        if (!isValidWrite(checkOutDate, createdAt)) {
//            throw new ReviewException(ErrorType.FAILED_TO_CREATE_REVIEW);
//        }
//    }
//
//    private BigDecimal calculateGrade(List<Review> reviews, Accommodation accommodation) {
//        if (reviews.isEmpty()) {
//            return accommodation.getGrade(); //return BigDecimal.ZERO;
//        }
//        BigDecimal totalGrade = reviews.stream()
//            .map(Review::getGrade)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return totalGrade.divide(BigDecimal.valueOf(reviews.size()), 1, RoundingMode.HALF_UP);
//    }
}
