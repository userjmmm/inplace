package team7.inplace.post.persistence;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.liked.likedPost.domain.QLikedPost;
import team7.inplace.post.domain.QPost;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;
import team7.inplace.post.persistence.dto.QPostQueryResult_DetailedPost;
import team7.inplace.user.domain.QUser;

@Repository
@RequiredArgsConstructor
public class PostReadRepositoryImpl implements PostReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CursorResult<DetailedPost> findPostsOrderByCreatedDate(
        Long userId,
        Long cursorId,
        int size,
        String orderBy
    ) {
        var posts = buildDetailedPostQuery(userId, orderBy)
            .where(cursorId == null ? null : QPost.post.id.lt(cursorId))
            .orderBy(getOrderSpecifier(orderBy))
            .limit(size + 1)
            .fetch();

        boolean hasNext = posts.size() > size;
        return new CursorResult<>(
            posts.subList(0, Math.min(size, posts.size())),
            hasNext,
            hasNext ? posts.get(size - 1).cursorId() : null
        );

    }

    private JPAQuery<DetailedPost> buildDetailedPostQuery(Long userId, String orderBy) {
        var likedJoinCondition = QLikedPost.likedPost.postId.eq(QPost.post.id);
        if (userId != null) {
            likedJoinCondition = likedJoinCondition.and(QLikedPost.likedPost.userId.eq(userId));
        }
        return queryFactory
            .select(new QPostQueryResult_DetailedPost(
                    QPost.post.id,
                    QUser.user.nickname,
                    QUser.user.profileImageUrl,
                    QPost.post.title.title,
                    QPost.post.content.content,
                    QPost.post.photos.imageInfos,
                    QLikedPost.likedPost.id.isNotNull(),
                    QPost.post.totalLikeCount,
                    QPost.post.totalCommentCount,
                    Expressions.FALSE,
                    QPost.post.createdAt,
                    getCursorPath(orderBy)
                )
            )
            .from(QPost.post)
            .innerJoin(QUser.user).on(QPost.post.authorId.eq(QUser.user.id))
            .leftJoin(QLikedPost.likedPost).on(likedJoinCondition);
    }

    private OrderSpecifier<?> getOrderSpecifier(String orderBy) {
        return switch (orderBy) {
            default -> QPost.post.id.desc();
        };
    }

    private NumberPath<Long> getCursorPath(String orderBy) {
        return switch (orderBy) {
            default -> QPost.post.id;
        };
    }
}
