package team7.inplace.user.presentation.dto;

import java.util.Date;
import team7.inplace.review.application.dto.MyReviewInfo;
import team7.inplace.review.application.dto.MyReviewInfo.ReviewPlaceInfo;

public record MyReviewResponse(
    Long reviewId,
    boolean likes,
    String comment,
    Date createdDate,
    ReviewPlaceInfo place
) {

    public static MyReviewResponse from(MyReviewInfo reviewInfo) {
        return new MyReviewResponse(
            reviewInfo.reviewId(),
            reviewInfo.likes(),
            reviewInfo.comment(),
            reviewInfo.createdDate(),
            reviewInfo.placeInfo()
        );
    }
}
