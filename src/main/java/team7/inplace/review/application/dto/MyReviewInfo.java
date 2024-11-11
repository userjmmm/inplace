package team7.inplace.review.application.dto;

import java.util.Date;
import team7.inplace.place.application.dto.PlaceInfo.AddressInfo;
import team7.inplace.place.domain.Place;
import team7.inplace.review.domain.Review;


public record MyReviewInfo(
    Long reviewId,
    boolean likes,
    String comment,
    Date createdDate,
    ReviewPlaceInfo placeInfo
) {

    public record ReviewPlaceInfo(
        Long placeId,
        String imgUrl,
        AddressInfo address
    ) {

        public static ReviewPlaceInfo from(Place place) {
            return new ReviewPlaceInfo(
                place.getId(),
                place.getMenuImgUrl(),
                AddressInfo.of(place.getAddress())
            );
        }
    }

    public static MyReviewInfo from(Review review) {
        return new MyReviewInfo(
            review.getId(),
            review.isLiked(),
            review.getComment(),
            review.getCreatedDate(),
            ReviewPlaceInfo.from(review.getPlace())
        );
    }
}
