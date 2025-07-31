package video.query;

public record VideoQueryParam(
    Boolean registered,
    Long influencerId
) {

    public static VideoQueryParam of(Boolean registered, Long influencerId) {
        return new VideoQueryParam(registered, influencerId);
    }
}
