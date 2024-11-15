package team7.inplace.search.persistence;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import team7.inplace.influencer.domain.Influencer;
import team7.inplace.influencer.domain.QInfluencer;
import team7.inplace.search.persistence.dto.SearchResult;

@Repository
@RequiredArgsConstructor
public class InfluencerSearchRepository implements SearchRepository<Influencer> {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchResult<Influencer>> searchEntityByKeywords(String keyword) {
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
                .limit(SEARCH_LIMIT)
                .fetch()
                .stream()
                .map(tuple -> new SearchResult<>(
                        tuple.get(0, Influencer.class),
                        tuple.get(1, Double.class)
                )).toList();
    }

    public Page<SearchResult<Influencer>> searchEntityByKeywords(String keyword, Pageable pageable) {
        NumberTemplate<Double> matchScore = Expressions.numberTemplate(
                Double.class,
                "function('match_against', {0}, {1})",
                QInfluencer.influencer.name,
                keyword
        );

        // 결과 조회
        List<SearchResult<Influencer>> content = queryFactory
                .select(
                        QInfluencer.influencer,
                        matchScore.as("score")
                )
                .from(QInfluencer.influencer)
                .where(matchScore.gt(0))
                .orderBy(matchScore.desc())
                .offset(pageable.getOffset())   // 페이지 오프셋
                .limit(pageable.getPageSize())  // 페이지 사이즈
                .fetch()
                .stream()
                .map(tuple -> new SearchResult<>(
                        tuple.get(0, Influencer.class),
                        tuple.get(1, Double.class)
                )).toList();

        // 전체 카운트 조회
        long total = queryFactory
                .select(QInfluencer.influencer.count())
                .from(QInfluencer.influencer)
                .where(matchScore.gt(0))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
