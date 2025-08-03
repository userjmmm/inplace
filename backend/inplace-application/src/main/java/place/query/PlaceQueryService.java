package place.query;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.query.PlaceQueryResult.DetailedPlace;
import place.query.dto.PlaceParam.Coordinate;
import place.query.dto.PlaceParam.Filter;
import place.query.dto.PlaceParam.Region;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceQueryService {

    private final PlaceReadRepository placeReadRepository;

    public Page<DetailedPlace> getPlacesInMapRange(
        Long userId,
        PlaceQueryParam.Coordinate coordinate,
        Filter filterParamsCommand,
        Pageable pageable
    ) {

        var coordinate = coordinateCommand.toQueryParam();
        var filterQueryParam = filterParamsCommand.toQueryParam();

        var placeQueryResult = placeReadRepository.findPlacesInMapRangeWithPaging(
            coordinate.topLeftLongitude(),
            coordinate.topLeftLatitude(),
            coordinate.bottomRightLongitude(),
            coordinate.bottomRightLatitude(),
            coordinate.longitude(),
            coordinate.latitude(),
            regionFilters,
            categoryFilters,
            influencerFilters,
            pageable,
            userId
        );

        // 위치와 필터링으로 Place 조회
        var placesPage = getPlacesByDistance(
            coordinateCommand, regionFilters, categoryFilters, influencerFilters,
            pageable, userId);
        if (placesPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        return placesPage;
    }

    private Page<PlaceQueryResult.DetailedPlace> getPlacesByDistance(
        Coordinate placesCoordinateCommand,
        List<Region> regions,
        List<Long> categoryFilters,
        List<String> influencerFilters,
        Pageable pageable,
        Long userId
    ) {
        return placeReadRepository.findPlacesInMapRangeWithPaging(
            placesCoordinateCommand.topLeftLongitude(),
            placesCoordinateCommand.topLeftLatitude(),
            placesCoordinateCommand.bottomRightLongitude(),
            placesCoordinateCommand.bottomRightLatitude(),
            placesCoordinateCommand.longitude(),
            placesCoordinateCommand.latitude(),
            regions,
            categoryFilters,
            influencerFilters,
            pageable,
            userId
        );
    }
}
