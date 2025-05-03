package team7.inplace.place.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.liked.likedPlace.domain.QLikedPlace;
import team7.inplace.place.application.command.PlacesCommand.RegionParam;
import team7.inplace.place.domain.Category;
import team7.inplace.place.domain.QPlace;
import team7.inplace.place.domain.QPlaceVideo;
import team7.inplace.place.persistence.dto.PlaceQueryResult;
import team7.inplace.place.persistence.dto.PlaceQueryResult.DetailedPlace;
import team7.inplace.place.persistence.dto.PlaceQueryResult.Marker;
import team7.inplace.place.persistence.dto.PlaceQueryResult.MarkerDetail;
import team7.inplace.place.persistence.dto.PlaceQueryResult.SimplePlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_DetailedPlace;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_Marker;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_MarkerDetail;
import team7.inplace.place.persistence.dto.QPlaceQueryResult_SimplePlace;
import team7.inplace.video.domain.QVideo;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PlaceReadRepositoryImpl implements PlaceReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PlaceQueryResult.DetailedPlace> findDetailedPlaceById(
        Long userId,
        Long placeId
    ) {
        var detailedPlace = getDetailedPlaceById(userId, placeId);
        if (detailedPlace == null) {
            return Optional.empty();
        }

        return Optional.of(detailedPlace);
    }

    @Override
    public Page<PlaceQueryResult.DetailedPlace> findPlacesInMapRangeWithPaging(
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude,
        Double longitude, Double latitude,
        List<RegionParam> regionFilters,
        List<Category> categoryFilters,
        List<String> influencerFilters,
        Pageable pageable,
        Long userId
    ) {
        List<Long> filteredPlaceId = getFilteredPlaceIds(
            topLeftLongitude, topLeftLatitude,
            bottomRightLongitude, bottomRightLatitude,
            regionFilters, categoryFilters, influencerFilters
        );

        if (filteredPlaceId.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<DetailedPlace> places = getDetailedPlacesByIds(pageable, userId, filteredPlaceId);

        return PageableExecutionUtils.getPage(places, pageable, filteredPlaceId::size);
    }

    @Override
    public List<Marker> findPlaceLocationsInMapRange(
        Double topLeftLongitude,
        Double topLeftLatitude,
        Double bottomRightLongitude,
        Double bottomRightLatitude,
        List<RegionParam> regionParams,
        List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        var filteredPlaceIds = getFilteredPlaceIds(
            topLeftLongitude, topLeftLatitude,
            bottomRightLongitude, bottomRightLatitude,
            regionParams, categoryFilters, influencerFilters
        );

        return getPlaceMarkersInIds(filteredPlaceIds);
    }

    @Override
    public Optional<SimplePlace> findSimplePlaceById(Long placeId) {
        var simplePlace = jpaQueryFactory
            .select(new QPlaceQueryResult_SimplePlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QPlace.place)
            .where(
                QPlace.place.id.eq(placeId),
                QPlace.place.deleteAt.isNull()
            )
            .fetchOne();
        if (simplePlace == null) {
            return Optional.empty();
        }
        return Optional.of(simplePlace);
    }


    @Override
    public Page<PlaceQueryResult.DetailedPlace> findLikedPlacesByUserIdWithPaging(
        Long userId,
        Pageable pageable
    ) {
        var likedPlaceIds = jpaQueryFactory
            .select(QLikedPlace.likedPlace.id).distinct()
            .from(QLikedPlace.likedPlace)
            .where(
                QLikedPlace.likedPlace.userId.eq(userId),
                QLikedPlace.likedPlace.isLiked.isTrue(),
                QLikedPlace.likedPlace.deleteAt.isNull()
            )
            .fetch();

        if (likedPlaceIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        var likedPlaces = getDetailedPlacesByIds(pageable, userId, likedPlaceIds);

        return PageableExecutionUtils.getPage(likedPlaces, pageable, likedPlaceIds::size);
    }

    @Override
    public MarkerDetail findPlaceMarkerById(Long placeId) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_MarkerDetail(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.category,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3
            ))
            .from(QPlace.place)
            .where(QPlace.place.id.eq(placeId))
            .fetchOne();
    }

    @Override
    public List<Marker> findPlaceLocationsByName(
        String name,
        List<RegionParam> regionParams,
        List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        var filteredPlaceId = getFilteredPlaceIdsByName(name, regionParams, categoryFilters,
            influencerFilters);

        return getPlaceMarkersInIds(filteredPlaceId);
    }

    @Override
    public Page<DetailedPlace> findPlacesByNameWithPaging(
        Long userId, String name,
        List<RegionParam> regions, List<Category> categories, List<String> influencers,
        Pageable pageable
    ) {
        var filteredPlaceId = getFilteredPlaceIdsByName(name, regions, categories, influencers);

        if (filteredPlaceId.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        var places = getDetailedPlacesByIds(pageable, userId, filteredPlaceId);
        if (places.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        return PageableExecutionUtils.getPage(places, pageable, filteredPlaceId::size);
    }

    private DetailedPlace getDetailedPlaceById(Long userId, Long placeId) {
        QLikedPlace likedPlace = new QLikedPlace("likedPlace");
        QLikedPlace selfLikedPlace = new QLikedPlace("selfLikedPlace");
        return jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QPlace.place.category,
                QPlace.place.googlePlaceId,
                QPlace.place.kakaoPlaceId,
                likedPlace.id.countDistinct(),
                Expressions.numberTemplate(Integer.class, "MAX(CASE WHEN {0} THEN 1 ELSE 0 END)",
                    selfLikedPlace.id.isNotNull()).eq(1)
            ))
            .from(QPlace.place)
            .leftJoin(likedPlace).on(likedPlace.placeId.eq(QPlace.place.id)
                .and(likedPlace.isLiked.isTrue()))
            .leftJoin(selfLikedPlace).on(selfLikedPlace.placeId.eq(QPlace.place.id)
                .and(userId != null ?
                    selfLikedPlace.userId.eq(userId) :
                    selfLikedPlace.userId.isNull())
                .and(selfLikedPlace.isLiked.isTrue()))
            .where(
                QPlace.place.id.eq(placeId),
                QPlace.place.deleteAt.isNull()
            )
            .groupBy(QPlace.place.id)
            .fetchOne();
    }

    private List<DetailedPlace> getDetailedPlacesByIds(
        Pageable pageable, Long userId, List<Long> filteredPlaceId
    ) {
        QLikedPlace likedPlace = new QLikedPlace("likedPlace");
        QLikedPlace selfLikedPlace = new QLikedPlace("selfLikedPlace");
        return jpaQueryFactory
            .select(new QPlaceQueryResult_DetailedPlace(
                QPlace.place.id,
                QPlace.place.name,
                QPlace.place.address.address1,
                QPlace.place.address.address2,
                QPlace.place.address.address3,
                QPlace.place.coordinate.longitude,
                QPlace.place.coordinate.latitude,
                QPlace.place.category,
                QPlace.place.googlePlaceId,
                QPlace.place.kakaoPlaceId,
                likedPlace.id.countDistinct(),
                Expressions.numberTemplate(Integer.class, "MAX(CASE WHEN {0} THEN 1 ELSE 0 END)",
                    selfLikedPlace.id.isNotNull()).eq(1)
            ))
            .from(QPlace.place)
            .leftJoin(likedPlace)
            .on(likedPlace.placeId.eq(QPlace.place.id).and(likedPlace.isLiked.isTrue()))
            .leftJoin(selfLikedPlace).on(selfLikedPlace.placeId.eq(QPlace.place.id)
                .and(userId != null ?
                    selfLikedPlace.userId.eq(userId) :
                    selfLikedPlace.userId.isNull()
                ).and(selfLikedPlace.isLiked.isTrue())
            )
            .where(
                QPlace.place.id.in(filteredPlaceId),
                QLikedPlace.likedPlace.deleteAt.isNull()
            )
            .groupBy(QPlace.place.id)
            .orderBy(QPlace.place.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    private List<Marker> getPlaceMarkersInIds(List<Long> filteredPlaceIds) {
        return jpaQueryFactory
            .select(new QPlaceQueryResult_Marker(
                    QPlace.place.id,
                    QPlace.place.coordinate.longitude,
                    QPlace.place.coordinate.latitude
                )
            )
            .from(QPlace.place)
            .where(QPlace.place.id.in(filteredPlaceIds))
            .fetch();
    }

    private List<Long> getFilteredPlaceIds(
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude,
        List<RegionParam> regionFilters, List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        var locationCondition = locationRegionCondition( // 주소 or 바운더리 검색 조건
            regionFilters,
            topLeftLongitude, topLeftLatitude,
            bottomRightLongitude, bottomRightLatitude
        );
        var filterExpression = createFilters(categoryFilters, influencerFilters);
        /* 조건에 맞는 장소 ID 목록 조회 */
        return jpaQueryFactory
            .select(QPlace.place.id).distinct()
            .from(QPlace.place)
            .leftJoin(QPlaceVideo.placeVideo).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .leftJoin(QVideo.video).on(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(
                locationCondition,
                filterExpression,
                QPlace.place.deleteAt.isNull(),
                QVideo.video.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull()
            ).fetch();
    }

    private List<Long> getFilteredPlaceIdsByName(
        String name,
        List<RegionParam> regionParams,
        List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        var locationCondition = locationRegionCondition(regionParams, null, null, null, null);
        var filterExpression = createFilters(categoryFilters, influencerFilters);
        var searchCondition = getMatchScore(name);

        return jpaQueryFactory
            .select(QPlace.place.id).distinct()
            .from(QPlace.place)
            .leftJoin(QPlaceVideo.placeVideo).on(QPlaceVideo.placeVideo.placeId.eq(QPlace.place.id))
            .leftJoin(QVideo.video).on(QPlaceVideo.placeVideo.videoId.eq(QVideo.video.id))
            .leftJoin(QInfluencer.influencer)
            .on(QVideo.video.influencerId.eq(QInfluencer.influencer.id))
            .where(
                locationCondition,
                filterExpression,
                searchCondition.gt(0),
                QPlace.place.deleteAt.isNull(),
                QVideo.video.deleteAt.isNull(),
                QInfluencer.influencer.deleteAt.isNull()
            ).fetch();
    }

    private BooleanBuilder locationRegionCondition(
        List<RegionParam> regionParams,
        Double topLeftLongitude, Double topLeftLatitude,
        Double bottomRightLongitude, Double bottomRightLatitude
    ) {
        BooleanBuilder expression = new BooleanBuilder();

        if (regionParams != null && !regionParams.isEmpty()) {
            BooleanBuilder regionBuilder = new BooleanBuilder();
            for (RegionParam region : regionParams) {
                BooleanExpression cityCondition = QPlace.place.address.address1.eq(region.city());
                BooleanExpression districtCondition = region.district() == null // '전체'인 경우
                    ? Expressions.TRUE // address1만 체크하도록 설정
                    : QPlace.place.address.address2.eq(region.district());
                regionBuilder.or(cityCondition.and(districtCondition));
            }
            expression.and(regionBuilder);

            return expression;
        }
        if (topLeftLongitude == null || topLeftLatitude == null ||
            bottomRightLongitude == null || bottomRightLatitude == null
        ) {
            return expression;
        }
        // 지역 필터가 없으면 기존의 바운더리로 검색
        expression.and(
            QPlace.place.coordinate.longitude.between(topLeftLongitude, bottomRightLongitude));
        expression.and(
            QPlace.place.coordinate.latitude.between(bottomRightLatitude, topLeftLatitude));

        return expression;
    }

    private BooleanBuilder createFilters(
        List<Category> categoryFilters,
        List<String> influencerFilters
    ) {
        BooleanBuilder expression = new BooleanBuilder();
        if (categoryFilters != null && !categoryFilters.isEmpty()) {
            expression.and(QPlace.place.category.in(categoryFilters));
        }

        if (influencerFilters != null && !influencerFilters.isEmpty()) {
            expression.and(QInfluencer.influencer.name.in(influencerFilters));
        }

        return expression;
    }

    private NumberTemplate<Double> getMatchScore(String keyword) {
        return Expressions.numberTemplate(
            Double.class,
            "function('match_against', {0}, {1})",
            QPlace.place.name,
            keyword
        );
    }
}