package team7.inplace.search.persistence;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.place.domain.QPlace;
import team7.inplace.search.persistence.dto.SearchResult;
import team7.inplace.video.domain.QVideo;
import team7.inplace.video.domain.Video;

@Repository
@RequiredArgsConstructor
public class VideoSearchRepository implements SearchRepository<Video> {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchResult<Video>> searchSimilarKeywords(String keyword) {
        var placeKeywordSearchResults = searchSimilarKeywordWithPlace(keyword);
        var influencerKeywordSearchResults = searchSimilarKeywordWithInfluencer(keyword);

        return Stream.concat(placeKeywordSearchResults.stream(), influencerKeywordSearchResults.stream())
                .distinct()
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .limit(AUTO_COMPLETION_LIMIT)
                .toList();
    }

    private List<SearchResult<Video>> searchSimilarKeywordWithPlace(String keyword) {
        NumberTemplate<Double> matchScore = Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1})",
                QPlace.place.name,
                keyword
        );

        return queryFactory
                .select(QVideo.video,
                        matchScore.as("score")
                )
                .from(QVideo.video)
                .join(QVideo.video.place, QPlace.place).fetchJoin()
                .join(QVideo.video.influencer, QInfluencer.influencer).fetchJoin()
                .where(matchScore.gt(0))
                .orderBy(matchScore.desc())
                .limit(AUTO_COMPLETION_LIMIT)
                .fetch()
                .stream()
                .map(tuple -> new SearchResult<>(
                        tuple.get(0, Video.class),
                        tuple.get(1, Double.class)
                )).toList();
    }

    private List<SearchResult<Video>> searchSimilarKeywordWithInfluencer(String keyword) {
        NumberTemplate<Double> matchScore = Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1})",
                QInfluencer.influencer.name,
                keyword
        );

        return queryFactory
                .select(QVideo.video,
                        matchScore.as("score")
                )
                .from(QVideo.video)
                .join(QVideo.video.place, QPlace.place).fetchJoin()
                .join(QVideo.video.influencer, QInfluencer.influencer).fetchJoin()
                .where(matchScore.gt(0))
                .orderBy(matchScore.desc())
                .limit(AUTO_COMPLETION_LIMIT)
                .fetch()
                .stream()
                .map(tuple -> new SearchResult<>(
                        tuple.get(0, Video.class),
                        tuple.get(1, Double.class)
                )).toList();
    }
}
