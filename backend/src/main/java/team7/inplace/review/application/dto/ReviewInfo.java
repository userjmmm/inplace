package team7.inplace.review.application.dto;

import team7.inplace.review.domain.Review;

import java.util.Date;

public record ReviewInfo(
        Long reviewId,
        boolean likes,
        String comment,
        String userNickname,
        Date createdDate,
        boolean mine
) {

    public static ReviewInfo from(Review review, boolean isMine) {
        return new ReviewInfo(
                review.getId(),
                review.isLiked(),
                review.getComment(),
                review.getUser().getNickname(),
                review.getCreatedDate(),
                isMine
        );
    }
}
