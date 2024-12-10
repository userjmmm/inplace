package team7.inplace.place.presentation.dto;

import team7.inplace.review.application.dto.ReviewInfo;

import java.util.Date;

public record ReviewResponse(
        Long reviewId,
        boolean likes,
        String comment,
        String userNickname,
        Date createdDate,
        boolean mine
) {

    public static ReviewResponse from(ReviewInfo reviewInfo) {
        return new ReviewResponse(
                reviewInfo.reviewId(),
                reviewInfo.likes(),
                reviewInfo.comment(),
                reviewInfo.userNickname(),
                reviewInfo.createdDate(),
                reviewInfo.mine()
        );
    }
}
