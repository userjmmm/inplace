package my.inplace.infra.place;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import my.inplace.domain.place.query.PlaceQueryResult;
import my.inplace.infra.config.AbstractMySQLContainer;
import my.inplace.infra.config.TestQueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = PlaceReadQueryDslRepository.class
    )
)
@ActiveProfiles("test-mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/sql/test-place.sql")
@Import(TestQueryDslConfig.class)
@EntityScan("my.inplace.domain")
class PlaceReadQueryDslRepositoryTest extends AbstractMySQLContainer {

    @Autowired
    PlaceReadQueryDslRepository placeReadRepository;

    @Test
    void findDetailedPlaceById() {
        // given
        Long placeId = 1L;
        Long userId = 1L;
        PlaceQueryResult.DetailedPlace expected = new PlaceQueryResult.DetailedPlace(
            1L, "테스트장소1", "주소1-1", "주소2-1", "주소3", 126.0, 36.0, "맛집", "googlePlaceId1", 1L, 1L, false
        );

        // when
        Optional<PlaceQueryResult.DetailedPlace> actual = placeReadRepository.findDetailedPlaceById(userId, placeId);

        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void findPlacesInMapRangeWithPaging() {
    }

    @Test
    void findPlaceLocationsInMapRange() {
    }

    @Test
    void findSimplePlaceById() {
    }

    @Test
    void findLikedPlacesByUserIdWithPaging() {
    }

    @Test
    void findPlaceMarkerById() {
    }

    @Test
    void findPlaceLocationsByName() {
    }

    @Test
    void findPlacesByNameWithPaging() {
    }

    @Test
    void getDetailedPlacesByVideoId() {
    }
}
