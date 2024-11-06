package team7.inplace.placeMessage.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMessageMaker {

    private final ObjectMapper objectMapper;

    public Map<String, List<String>> createLocationTemplate() {
        try {
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            Link link = Link.of("webUrl", "mobileWebUrl", "android", "ios");
            Content content = Content.of("title", "imageUrl", "description", link);
            LocationTemplate locationTemplate = new LocationTemplate("location", "address",
                "addressTitle", content, "buttonTitle", new ArrayList<Button>());
            body.add("template_object", objectMapper.writeValueAsString(locationTemplate));
            return body;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
