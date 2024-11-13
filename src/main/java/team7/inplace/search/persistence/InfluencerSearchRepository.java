package team7.inplace.search.persistence;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.search.persistence.dto.SearchResult;

@Repository
@RequiredArgsConstructor
public class InfluencerSearchRepository implements SearchRepository<Influencer> {
    private final JPAQueryFactory queryFactory;

    public List<SearchResult<Influencer>> searchSimilarKeywords(String keyword) {
        NumberTemplate<Double> matchScore = Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1})",
                QInfluencer.influencer.name,
                keyword
        );

        return queryFactory
                .select(
                        QInfluencer.influencer,
                        matchScore.as("score")
                )
                .from(QInfluencer.influencer)
                .where(matchScore.gt(0))
                .orderBy(matchScore.desc())
                .limit(AUTO_COMPLETION_LIMIT)
                .fetch()
                .stream()
                .map(tuple -> new SearchResult<>(
                        tuple.get(QInfluencer.influencer),
                        tuple.get(matchScore)
                )).toList();
    }
}
