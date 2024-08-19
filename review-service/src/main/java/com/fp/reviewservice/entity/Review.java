package com.fp.reviewservice.entity;

import com.fp.reviewservice.dto.request.ReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long roomId;

    private Long memberId;

    private Long reservationId;

    public static Review from(ReviewRequest request, Long roomId,
        Long memberId) {
     return Review.builder()
         .title(request.getTitle())
         .comment(request.getComment())
         .createdAt(LocalDateTime.now())
         .roomId(roomId)
         .memberId(memberId)
         .reservationId(request.getReservationId())
         .build();
    }

    public void update(ReviewRequest request){
        this.title = request.getTitle();
        this.comment  = request.getComment();
        this.updatedAt = LocalDateTime.now();
    }
}
