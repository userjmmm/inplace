package team7.inplace.placeMessage.application.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import team7.inplace.place.application.dto.PlaceDetailInfo;
import team7.inplace.place.domain.Category;
import team7.inplace.video.application.AliasUtil;

public record PlaceMessageCommand(
    Long placeId,
    String title,
    String address,
    String videoUrl,
    String imageUrl,
    String description
) {

    private static final String REGEX = "(?:https?:\\/\\/)?(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w-]*)(&(amp;)?[\\w?=]*)?";

    public static PlaceMessageCommand of(PlaceDetailInfo placeDetailInfo) {
        return new PlaceMessageCommand(
            placeDetailInfo.placeInfo().placeId(),
            placeDetailInfo.placeInfo().placeName(),
            placeDetailInfo.placeInfo().address().getAddress(),
            placeDetailInfo.videoUrl(),
            videoUrlToImgUrl(placeDetailInfo.videoUrl()),
            AliasUtil.makeAlias(
                placeDetailInfo.placeInfo().influencerName(),
                Category.valueOf(placeDetailInfo.placeInfo().category())
            )
        );
    }

    public static String videoUrlToImgUrl(String videoUrl) {
        Matcher matcher = Pattern.compile(REGEX).matcher(videoUrl);

        if (matcher.find()) {
            String videoId = matcher.group(1);
            return String.format("https://img.youtube.com/vi/%s/maxresdefault.jpg", videoId);
        } else {
            return null;
        }
    }
}
