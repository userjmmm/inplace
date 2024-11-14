package team7.inplace.user.presentation.dto;

import team7.inplace.place.application.dto.LikedPlaceInfo;

public record LikedPlaceResponse(
        Long placeId,
        String placeName,
        String imageUrl,
        String influencerName,
        boolean likes
) {

    public static LikedPlaceResponse from(LikedPlaceInfo likedPlaceInfo) {
        return new LikedPlaceResponse(
                likedPlaceInfo.placeId(),
                likedPlaceInfo.placeName(),
                likedPlaceInfo.imageUrl(),
                likedPlaceInfo.influencerName(),
                likedPlaceInfo.likes()
        );
    }
}
