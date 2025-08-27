package crawling.dto;


import crawling.command.PlaceRegistrationCommand;

public record PlaceRegistrationRequest(
    Long videoId,
    Long placeUUID,
    String category
) {

    public PlaceRegistrationCommand toCommand() {
        return new PlaceRegistrationCommand(videoId, placeUUID, category);
    }
}
