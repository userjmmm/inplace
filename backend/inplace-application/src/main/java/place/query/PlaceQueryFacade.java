package place.query;

import annotation.Facade;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import place.dto.PlaceInfo;
import place.dto.PlaceInfo.Simple;
import place.query.PlaceQueryResult.Marker;
import place.query.PlaceQueryResult.MarkerDetail;
import place.query.dto.PlaceParam;
import video.VideoService;

@Facade
@RequiredArgsConstructor
public class PlaceQueryFacade {

    private final PlaceQueryService placeQueryService;
    private final VideoService videoService;
    private final Executor externalApiExecutor;

    public PlaceInfo.Detail getDetailedPlaces(
        Long placeId
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var googlePlaceId = placeQueryService.getGooglePlaceId(placeId);
        if (googlePlaceId.isEmpty()) {
            var placeInfo = placeQueryService.getPlaceInfo(userId, placeId);

            var videoSearchParam = VideoSearchParams.from(
                placeInfo.longitude().toString(),
                placeInfo.latitude().toString()
            );
            var surroundVideos = videoService.getVideosBySurround(
                videoSearchParam,
                PageRequest.of(0, 10),
                false
            );
            var videoInfos = videoService.getVideosByPlaceId(placeId);
            var reviewRates = reviewService.getReviewLikeRate(placeId);
            return PlaceInfo.Detail.of(placeInfo, null, videoInfos, reviewRates, surroundVideos);
        }

        var googlePlace = CompletableFuture.supplyAsync(
            () -> placeQueryService.getGooglePlaceInfo(googlePlaceId.get()),
            externalApiExecutor
        ).exceptionally(e -> null);

        var placeInfo = placeQueryService.getPlaceInfo(userId, placeId);
        var surroundVideos = videoService.getVideosBySurround(
            VideoSearchParams.from(placeInfo.longitude().toString(),
                placeInfo.latitude().toString()),
            PageRequest.of(0, 10),
            false
        );
        var videoInfos = videoService.getVideosByPlaceId(placeId);
        var reviewRates = reviewService.getReviewLikeRate(placeId);

        return PlaceInfo.Detail.of(placeInfo, googlePlace.join(), videoInfos, reviewRates,
            surroundVideos);
    }

    public Page<Simple> getPlacesInMapRange(
        PlaceParam.Coordinate coordinate,
        PlaceParam.Filter filterParams,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeQueryService.getPlacesInMapRange(
            userId,
            coordinate,
            filterParams,
            pageable
        );
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos.map(
            place -> PlaceInfo.Simple.of(place, placeVideos.get(place.placeId()))
        );
    }

    public Page<Simple> getPlacesByName(
        String name,
        PlaceParam.Filter filterParams,
        Pageable pageable
    ) {
        var userId = AuthorizationUtil.getUserIdOrNull();

        var placeSimpleInfos = placeQueryService.getPlacesByName(userId, name, command, pageable);
        var placeIds = placeSimpleInfos.getContent()
            .stream()
            .map(PlaceQueryResult.DetailedPlace::placeId)
            .toList();
        var placeVideos = videoService.getVideosByPlaceId(placeIds);

        return placeSimpleInfos.map(
            place -> PlaceInfo.Simple.of(place, placeVideos.get(place.placeId()))
        );
    }

    public List<PlaceInfo.Category> getSubCategoriesByParentId(Long parentCategoryId) {
        return placeQueryService.getSubCategoriesByParentId(parentCategoryId);
    }

    /*
     * Marker 관련 매서드
     */
    public PlaceInfo.Marker getMarkerInfo(
        Long placeId
    ) {
        MarkerDetail markerDetail = placeQueryService.getMarkerInfo(placeId);
        var videos = videoService.getVideosByPlaceId(placeId);
        return PlaceInfo.Marker.of(markerDetail, videos);
    }

    public List<Marker> getPlaceLocations(
        PlaceParam.Coordinate coordinateParams,
        PlaceParam.Filter filterParams
    ) {
        return placeQueryService.getPlaceLocations(coordinateParams, filterParams);
    }

}
