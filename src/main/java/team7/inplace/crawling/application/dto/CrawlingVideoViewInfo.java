package team7.inplace.crawling.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record CrawlingVideoViewInfo(
        Long videoId,
        JsonNode videoDetail
) {
    public static CrawlingVideoViewInfo of(Long videoId, JsonNode videoDetail) {
        return new CrawlingVideoViewInfo(videoId, videoDetail);
    }
}