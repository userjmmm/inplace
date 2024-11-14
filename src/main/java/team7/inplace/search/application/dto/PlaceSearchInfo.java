package team7.inplace.search.application.dto;

import team7.inplace.place.domain.Place;

public record PlaceSearchInfo(
        Long placeId,
        String placeName,
        String imageUrl,
        Boolean likes
) {
    public static PlaceSearchInfo from(Place place, Boolean likes) {
        return new PlaceSearchInfo(
                place.getId(),
                place.getName(),
                place.getMenuImgUrl(),
                likes
        );
    }
}
