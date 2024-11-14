package team7.inplace.admin.crawling.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team7.inplace.admin.crawling.client.KakaoMapClient;
import team7.inplace.admin.crawling.client.dto.PlaceNode;

@Service
@RequiredArgsConstructor
public class KakaoCrawlingService {
    private final KakaoMapClient kakaoMapClient;

    public PlaceNode searchPlaceWithPlaceId(Long placeId) {
        return kakaoMapClient.searchPlaceWithPlaceId(placeId);
    }
}
