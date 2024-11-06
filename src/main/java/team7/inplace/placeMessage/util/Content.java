package team7.inplace.placeMessage.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Content(
    @NonNull String title,
    @NonNull @JsonProperty("image_url") String imageUrl,
    @NonNull String description,
    @NonNull Link link
) {

    public static Content of(String title, String imageUrl, String description, Link link) {
        return new Content(title, imageUrl, description, link);
    }
}
