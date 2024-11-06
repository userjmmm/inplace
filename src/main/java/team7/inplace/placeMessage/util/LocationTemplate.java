package team7.inplace.placeMessage.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record LocationTemplate(
    @NonNull @JsonProperty("object_type") String objectType,
    @NonNull String address,
    @Nullable @JsonProperty("address_title") String addressTitle,
    @NonNull Content content,
    @Nullable @JsonProperty("button_title") String buttonTitle,
    @Nullable List<Button> buttons
) {

}
