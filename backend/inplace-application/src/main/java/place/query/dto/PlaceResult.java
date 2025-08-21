package place.query.dto;

import java.util.List;
import place.client.GooglePlaceClientResponse;
import place.query.PlaceQueryResult;
import review.dto.ReviewResult;
import review.query.ReviewQueryResult;
import video.query.VideoQueryResult;
import video.query.dto.VideoResult;

public class PlaceResult {

    public record Detail(
        PlaceResult.DetailedPlace place,
        GooglePlaceResult.Place googlePlace,
        List<VideoResult.SimpleVideo> videos,
        ReviewResult.LikeRate reviewLikeRate,
        List<VideoResult.DetailedVideo> surroundVideos
    ) {

        public static PlaceResult.Detail of(
            PlaceQueryResult.DetailedPlace place,
            GooglePlaceClientResponse.Place googlePlace,
            List<VideoQueryResult.SimpleVideo> videos,
            ReviewQueryResult.LikeRate reviewLikeRate,
            List<VideoQueryResult.DetailedVideo> surroundVideos
        ) {
            return new PlaceResult.Detail(
                PlaceResult.DetailedPlace.from(place),
                GooglePlaceResult.Place.from(googlePlace),
                videos.stream().map(VideoResult.SimpleVideo::from).toList(),
                ReviewResult.LikeRate.from(reviewLikeRate),
                surroundVideos.stream().map(VideoResult.DetailedVideo::from).toList()
            );
        }
    }

    public record Simple(
        PlaceResult.DetailedPlace place,
        List<VideoResult.SimpleVideo> videos
    ) {

        public static PlaceResult.Simple of(
            PlaceQueryResult.DetailedPlace place,
            List<VideoQueryResult.SimpleVideo> videos
        ) {
            return new PlaceResult.Simple(
                PlaceResult.DetailedPlace.from(place),
                videos.stream().map(VideoResult.SimpleVideo::from).toList()
            );
        }
    }

    public record MarkerDetail(
        PlaceResult.MarkerPlace place,
        List<VideoResult.SimpleVideo> videos
    ) {

        public static PlaceResult.MarkerDetail of(
            PlaceQueryResult.MarkerDetail markerDetail,
            List<VideoResult.SimpleVideo> videos
        ) {
            return new PlaceResult.MarkerDetail(
                PlaceResult.MarkerPlace.from(markerDetail),
                videos
            );
        }

    }

    public record Category(
        Long id,
        Long parentId,
        String name,
        String engName
    ) {

        public static PlaceResult.Category from(place.Category category) {
            return new PlaceResult.Category(
                category.getId(),
                category.getParentId(),
                category.getName(),
                category.getEngName()
            );
        }
    }

    public record Marker(
        Long placeId,
        String parentsCategory,
        Double longitude,
        Double latitude
    ) {

        public static Marker from(PlaceQueryResult.Marker marker) {
            return new Marker(
                marker.placeId(),
                marker.parentsCategory(),
                marker.longitude(),
                marker.latitude()
            );
        }
    }

    public record DetailedPlace(
        Long placeId,
        String placeName,
        String address1,
        String address2,
        String address3,
        Double longitude,
        Double latitude,
        String category,
        String googlePlaceId,
        Long kakaoPlaceId,
        Long likeCount,
        Boolean isLiked
    ) {

        public static DetailedPlace from(PlaceQueryResult.DetailedPlace detailedPlace) {
            return new DetailedPlace(
                detailedPlace.placeId(),
                detailedPlace.placeName(),
                detailedPlace.address1(),
                detailedPlace.address2(),
                detailedPlace.address3(),
                detailedPlace.longitude(),
                detailedPlace.latitude(),
                detailedPlace.category(),
                detailedPlace.googlePlaceId(),
                detailedPlace.kakaoPlaceId(),
                detailedPlace.likeCount(),
                detailedPlace.isLiked()
            );
        }
    }

    public record MarkerPlace(
        Long placeId,
        String placeName,
        String category,
        String address1,
        String address2,
        String address3
    ) {

        public static MarkerPlace from(PlaceQueryResult.MarkerDetail place) {
            return new MarkerPlace(
                place.placeId(),
                place.placeName(),
                place.category(),
                place.address1(),
                place.address2(),
                place.address3()
            );
        }
    }
}
