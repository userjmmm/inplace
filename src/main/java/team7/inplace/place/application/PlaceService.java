package team7.inplace.place.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team7.inplace.LikedPlace.domain.LikedPlace;
import team7.inplace.LikedPlace.persistence.LikedPlaceRepository;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.global.exception.code.PlaceErrorCode;
import team7.inplace.global.exception.code.UserErroCode;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.place.application.command.PlaceLikeCommand;
import team7.inplace.place.application.command.PlacesCommand.Create;
import team7.inplace.place.application.command.PlacesCommand.PlacesCoordinateCommand;
import team7.inplace.place.application.command.PlacesCommand.PlacesFilterParamsCommand;
import team7.inplace.place.application.dto.PlaceDetailInfo;
import team7.inplace.place.application.dto.PlaceInfo;
import team7.inplace.place.domain.Place;
import team7.inplace.place.persistence.PlaceRepository;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.user.domain.User;
import team7.inplace.user.persistence.UserRepository;
import team7.inplace.video.domain.Video;
import team7.inplace.video.persistence.VideoRepository;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final VideoRepository videoRepository;

    private final UserRepository userRepository;

    private final LikedPlaceRepository likedPlaceRepository;

    public Page<PlaceInfo> getPlacesWithinRadius(
        PlacesCoordinateCommand placesCoordinateCommand,
        PlacesFilterParamsCommand placesFilterParamsCommand) {

        // categories와 influencers 필터 처리
        List<String> categoryFilters = placesFilterParamsCommand.isCategoryFilterExists()
            ? Arrays.stream(placesFilterParamsCommand.categories().split(",")).toList()
            : null;

        List<String> influencerFilters = placesFilterParamsCommand.isInfluencerFilterExists()
            ? Arrays.stream(placesFilterParamsCommand.influencers().split(",")).toList()
            : null;

        // 주어진 좌표로 장소를 찾고, 해당 페이지의 결과를 가져옵니다.
        Page<Place> placesPage = getPlacesByDistance(placesCoordinateCommand, categoryFilters,
            influencerFilters);

        // Place ID 목록 추출
        List<Long> placeIds = getPlaceIds(placesPage);

        // influencer 조회 => video->Map(placeId, influencerName)
        List<Video> videos = videoRepository.findByPlaceIdIn(placeIds);
        Map<Long, String> placeIdToInfluencerName = getMapPlaceIdToInfluencerName(
            videos);

        // PlaceInfo 생성
        List<PlaceInfo> placeInfos = convertToPlaceInfos(placesPage, placeIdToInfluencerName);

        // PlaceInfo 리스트를 Page로 변환하여 반환
        return new PageImpl<>(placeInfos, placesPage.getPageable(), placeInfos.size());
    }

    private List<PlaceInfo> convertToPlaceInfos(Page<Place> placesPage,
        Map<Long, String> placeIdToInfluencerName) {
        return placesPage.getContent().stream()
            .map(place -> {
                // map에서 조회되지 않은 placeId는 null로 처리
                String influencerName = placeIdToInfluencerName.getOrDefault(place.getId(), null);
                return PlaceInfo.of(place, influencerName);
            })
            .toList();
    }

    private Map<Long, String> getMapPlaceIdToInfluencerName(List<Video> videos) {
        return videos.stream()
            .collect(Collectors.toMap(
                video -> video.getPlace().getId(),
                video -> video.getInfluencer().getName(),
                (existing, replacement) -> existing
            ));
    }

    private List<Long> getPlaceIds(Page<Place> placesPage) {
        return placesPage.getContent().stream()
            .map(Place::getId)
            .toList();
    }

    private Page<Place> getPlacesByDistance(
        PlacesCoordinateCommand placesCoordinateCommand,
        List<String> categoryFilters,
        List<String> influencerFilters
    ) {
        return placeRepository.findPlacesByDistanceAndFilters(
            placesCoordinateCommand.topLeftLongitude(),
            placesCoordinateCommand.topLeftLatitude(),
            placesCoordinateCommand.bottomRightLongitude(),
            placesCoordinateCommand.bottomRightLatitude(),
            placesCoordinateCommand.longitude(),
            placesCoordinateCommand.latitude(),
            categoryFilters,
            influencerFilters,
            placesCoordinateCommand.pageable()
        );
    }

    public PlaceDetailInfo getPlaceDetailInfo(Long placeId) {
        Place place = placeRepository.findById(placeId)
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        Video video = null;
        List<Video> videos = videoRepository.findByPlaceId(placeId);

        if (!videos.isEmpty()) {
            video = videos.get(0);
        }
        Influencer influencer = (video != null) ? video.getInfluencer() : null;

        return PlaceDetailInfo.from(place, influencer, video);
    }

    public List<Long> createPlaces(List<Create> placeCommands) {
        var places = placeCommands.stream()
            .map(command -> {
                if (Objects.isNull(command)) {
                    return null;
                }
                return command.toEntity();
            })
            .toList();
        var nonNullPlaces = places.stream()
            .filter(Objects::nonNull)
            .toList();
        placeRepository.saveAll(nonNullPlaces);

        var savedPlacesId = places.stream()
            .map(place -> {
                if (Objects.isNull(place)) {
                    return -1L;
                }
                return place.getId();
            })
            .toList();

        return savedPlacesId;
    }

    public void likeToPlace(PlaceLikeCommand comm) {

        String username = AuthorizationUtil.getUsername();
        if (!StringUtils.hasText(username)) {
            throw InplaceException.of(AuthorizationErrorCode.TOKEN_IS_EMPTY);
        }
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> InplaceException.of(UserErroCode.NOT_FOUND));

        Place place = placeRepository.findById(comm.placeId())
            .orElseThrow(() -> InplaceException.of(PlaceErrorCode.NOT_FOUND));

        LikedPlace likedPlace = likedPlaceRepository.findByUserIdAndPlaceId(user.getId(),
                place.getId())
            .orElseGet(() -> new LikedPlace(user, place));

        likedPlace.updateLike(comm.likes());
        likedPlaceRepository.save(likedPlace);
    }
}
