package my.inplace.infra.influencer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import my.inplace.domain.influencer.query.InfluencerQueryResult;
import my.inplace.infra.config.TestQueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = InfluencerReadQueryDslRepository.class
    )
)
@Sql(scripts = "/sql/test-influencer.sql")
@Import(TestQueryDslConfig.class)
@EntityScan("my.inplace.domain")
class InfluencerReadQueryDslRepositoryTest {

    @Autowired
    InfluencerReadQueryDslRepository influencerRepository;


    @Test
    void getInfluencerDetail() {
        // given
        Long influencerId = 2L;
        Long userId = 1L;
        InfluencerQueryResult.Detail expected = new InfluencerQueryResult.Detail(
            2L, "인플루언서2", "img2", "직업2", true, 4L, 0L
        );

        // when
        Optional<InfluencerQueryResult.Detail> actual = influencerRepository.getInfluencerDetail(influencerId, userId);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    void getInfluencerNamesByPlaceId() {
        // given
        Long placeId = 1L;
        List<String> expected = List.of("인플루언서3", "인플루언서4");

        // when
        List<String> actual = influencerRepository.getInfluencerNamesByPlaceId(placeId);

        // then
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    void getInfluencerSortedByLikes() {
    }
}
