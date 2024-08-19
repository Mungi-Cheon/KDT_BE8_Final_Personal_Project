package com.fp.reviewservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String comment;

    @NotNull
    private BigDecimal grade;

    @NotNull
    private Long reservationId;

}
