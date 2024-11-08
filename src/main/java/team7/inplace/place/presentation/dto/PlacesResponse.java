package team7.inplace.place.presentation.dto;

import org.springframework.data.domain.Page;
import team7.inplace.place.application.dto.PlaceInfo;

public record PlacesResponse(Page<PlaceInfo> places) {

    public static PlacesResponse of(Page<PlaceInfo> places) {
        return new PlacesResponse(places);
    }
}