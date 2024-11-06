package team7.inplace.placeMessage.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record Link(
    @NonNull @JsonProperty("web_url") String webUrl,
    @NonNull @JsonProperty("mobile_web_url") String mobileWebUrl,
    @NonNull @JsonProperty("android_execution_params") String androidExecutionParams,
    @NonNull @JsonProperty("ios_execution_params") String iosExecutionParams
) {

    public static Link of(String webUrl, String mobileWebUrl, String androidExecutionParams,
        String iosExecutionParams) {
        return new Link(webUrl, mobileWebUrl, androidExecutionParams, iosExecutionParams);
    }
}
