package place.query.dto;

import java.util.List;
import place.client.GooglePlaceClientResponse;
import place.dto.PlaceInfo;
import place.query.PlaceQueryResult;
import place.query.PlaceQueryResult.MarkerDetail;
import review.query.ReviewQueryResult;
import video.query.VideoQueryResult;
import video.query.VideoQueryResult.SimpleVideo;

public class PlaceResult {

    public record Detail(
        PlaceQueryResult.DetailedPlace place,
        GooglePlaceClientResponse.Place googlePlace,
        List<SimpleVideo> videos,
        ReviewQueryResult.LikeRate reviewLikeRate,
        List<VideoQueryResult.DetailedVideo> surroundVideos
    ) {

        public static PlaceInfo.Detail of(
            PlaceQueryResult.DetailedPlace place,
            GooglePlaceClientResponse.Place googlePlace,
            List<VideoQueryResult.SimpleVideo> videos,
            ReviewQueryResult.LikeRate reviewLikeRate,
            List<VideoQueryResult.DetailedVideo> surroundVideos
        ) {
            return new PlaceInfo.Detail(
                place,
                googlePlace,
                videos,
                reviewLikeRate,
                surroundVideos
            );
        }
    }

    public record Simple(
        PlaceQueryResult.DetailedPlace place,
        List<SimpleVideo> video
    ) {

        public static PlaceInfo.Simple of(
            PlaceQueryResult.DetailedPlace place,
            List<VideoQueryResult.SimpleVideo> video
        ) {
            return new PlaceInfo.Simple(place, video);
        }
    }

    public record Marker(
        MarkerDetail place,
        List<VideoQueryResult.SimpleVideo> videos
    ) {

        public static PlaceInfo.Marker of(
            MarkerDetail markerDetail, List<VideoQueryResult.SimpleVideo> videos) {
            return new PlaceInfo.Marker(markerDetail, videos);
        }
    }

    public record Category(
        Long id,
        Long parentId,
        String name,
        String engName
    ) {

        public static PlaceInfo.Category from(place.Category category) {
            return new PlaceInfo.Category(
                category.getId(),
                category.getParentId(),
                category.getName(),
                category.getEngName()
            );
        }
    }
}
