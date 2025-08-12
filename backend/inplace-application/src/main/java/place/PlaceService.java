package place;

import exception.InplaceException;
import exception.code.AuthorizationErrorCode;
import exception.code.PlaceErrorCode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.client.GooglePlaceClientResponse;
import place.query.PlaceQueryResult;
import place.query.PlaceQueryResult.Marker;
import place.query.PlaceQueryResult.MarkerDetail;
import place.query.PlaceReadRepository;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand;
import team7.inplace.place.application.command.PlacesCommand.Coordinate;
import team7.inplace.place.application.command.PlacesCommand.FilterParams;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.application.command.PlacesCommand.Upsert;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.application.dto.PlaceInfo.Category;
import team7.inplace.place.client.RestTemplateGooglePlaceClient;
import team7.inplace.place.persistence.CategoryRepository;
import team7.inplace.place.persistence.LikedPlaceRepository;
import team7.inplace.place.persistence.PlaceJpaRepository;
import team7.inplace.place.persistence.PlaceVideoJpaRepository;
import video.query.VideoReadRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceReadRepository placeReadRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceVideoJpaRepository placeVideoJpaRepository;
    private final LikedPlaceRepository likedPlaceRepository;
    private final VideoReadRepository videoReadRepository;
    private final RestTemplateGooglePlaceClient restTemplateGooglePlaceClient;


    @Transactional(readOnly = true)
    public Page<PlaceQueryResult.DetailedPlace> getPlacesInMapRange(
        Long userId,
        Coordinate coordinateCommand,
        FilterParams filterParamsCommand,
        Pageable pageable
    ) {
        var regionFilters = filterParamsCommand.regions();
        var categoryFilters = filterParamsCommand.categories();
        var influencerFilters = filterParamsCommand.influencers();

        // 위치와 필터링으로 Place 조회
        var placesPage = getPlacesByDistance(
            coordinateCommand, regionFilters, categoryFilters, influencerFilters,
            pageable, userId);
        if (placesPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        return placesPage;
    }

    //TODO: 한 장소에 비디오가 여러개일 수 있으니 수정 필요
    @Transactional(readOnly = true)
    public PlaceInfo.Simple getPlaceMessageCommand(Long placeId, Long userId) {
        var place = placeReadRepository.findDetailedPlaceById(placeId, userId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
        var videos = videoReadRepository.findSimpleVideosByPlaceId(placeId);

        return PlaceInfo.Simple.of(place, videos);
    }

    @Transactional(readOnly = true)
    public Page<PlaceQueryResult.DetailedPlace> getLikedPlaceInfo(Long userId, Pageable pageable) {

        return placeReadRepository.findLikedPlacesByUserIdWithPaging(userId, pageable);
    }


    @Transactional(readOnly = true)
    public Optional<String> getGooglePlaceId(Long placeId) {
        return placeJpaRepository.findGooglePlaceIdById(placeId);
    }

    @Transactional(readOnly = true)
    public PlaceQueryResult.DetailedPlace getPlaceInfo(Long userId, Long placeId) {
        return placeReadRepository.findDetailedPlaceById(userId, placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));
    }

    public GooglePlaceClientResponse.Place getGooglePlaceInfo(String googlePlaceId) {
        return restTemplateGooglePlaceClient.requestForPlaceDetail(googlePlaceId);
    }

    }

    public Page<PlaceQueryResult.DetailedPlace> getPlacesByName(
        Long userId, String name, PlacesCommand.FilterParams command, Pageable pageable
    ) {
        return placeReadRepository.findPlacesByNameWithPaging(
            userId,
            name,
            command.regions(),
            command.categories(),
            command.influencers(),
            pageable
        );
    }

    public List<PlaceQueryResult.DetailedPlace> getSimplePlacesByVideoId(Long videoId) {
        return placeReadRepository.getDetailedPlacesByVideoId(videoId);
    }
}
