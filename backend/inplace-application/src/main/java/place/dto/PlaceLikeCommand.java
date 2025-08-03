package place.dto;

public record PlaceLikeCommand(
    Long placeId,
    Boolean likes
) {

}
