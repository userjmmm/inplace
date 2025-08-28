package my.inplace.api.review.dto;


import my.inplace.application.review.dto.ReviewInfo;

public class ReviewResponse {

    public record Invitation(
        String placeName,
        String placeAddress,
        String placeImgUrl,
        String influencerName,
        String userNickname
    ) {

        public static Invitation from(ReviewInfo.Invitation reviewInfo) {
            return new Invitation(
                reviewInfo.placeName(),
                reviewInfo.placeAddress(),
                "",
                reviewInfo.influencerName(),
                reviewInfo.userNickname()
            );
        }
    }
}
