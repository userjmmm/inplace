package my.inplace.infra.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import my.inplace.domain.review.query.ReviewQueryResult;
import my.inplace.domain.review.query.ReviewQueryResult.LikeRate;
import my.inplace.domain.review.query.ReviewQueryResult.Simple;
import my.inplace.infra.config.TestQueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
    includeFilters = @Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        value = ReviewReadQueryDslRepository.class
    )
)
@Import(TestQueryDslConfig.class)
@Sql("/sql/test-review.sql")
@EntityScan("my.inplace.domain")
class ReviewReadQueryDslRepositoryTest {

    @Autowired
    ReviewReadQueryDslRepository reviewReadRepository;

    @Test
    void findDetailedReviewByUserId() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<ReviewQueryResult.Detail> expected = List.of(
            new ReviewQueryResult.Detail(1L, true, "1->1 like", null, 1L, "테스트장소1", "주소1", "주소2", "주소3"),
            new ReviewQueryResult.Detail(6L, true, "1->2 like", null, 2L, "테스트장소2", "주소1", "주소2", "주소3")
        );

        // when
        Page<ReviewQueryResult.Detail> actual = reviewReadRepository.findDetailedReviewByUserId(userId, pageable);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }

    @Test
    void findSimpleReviewByUserIdAndPlaceId() {
        // given
        Long placeId = 1L;
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        List<ReviewQueryResult.Simple> expected = List.of(
            new ReviewQueryResult.Simple(1L, true, "1->1 like", "user1", null, true),
            new ReviewQueryResult.Simple(2L, true, "2->1 like", "user2", null, false),
            new ReviewQueryResult.Simple(3L, false, "3->1 dislike", "user3", null, false),
            new ReviewQueryResult.Simple(4L, false, "4->1 dislike", "user4", null, false),
            new ReviewQueryResult.Simple(5L, false, "5->1 dislike", "user5", null, false)
        );

        // when
        Page<ReviewQueryResult.Simple> actual = reviewReadRepository.findSimpleReviewByUserIdAndPlaceId(placeId, userId, pageable);

        // then
        assertThat(actual.getContent()).containsExactlyElementsOf(expected);
    }

    @Test
    void countRateByPlaceId() {
        // given
        Long placeId = 2L;
        ReviewQueryResult.LikeRate expected = new ReviewQueryResult.LikeRate(5, 0);

        // when
        ReviewQueryResult.LikeRate actual = reviewReadRepository.countRateByPlaceId(placeId);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
