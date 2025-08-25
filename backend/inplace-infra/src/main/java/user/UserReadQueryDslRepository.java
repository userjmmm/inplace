package user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import user.query.UserQueryResult;
import user.query.UserQueryResult.Badge;
import user.query.UserQueryResult.Simple;
import user.query.UserReadRepository;

@Repository
@RequiredArgsConstructor
public class UserReadQueryDslRepository implements UserReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Simple> findUserInfoById(Long id) {
        Simple userSimple = queryFactory
            .select(Projections.constructor(UserQueryResult.Simple.class,
                QUser.user.nickname,
                QUser.user.profileImageUrl,
                QTier.tier.name,
                QTier.tier.imgUrl,
                QBadge.badge.name,
                QBadge.badge.imgUrl
            ))
            .from(QUser.user)
            .innerJoin(QTier.tier).on(QTier.tier.id.eq(QUser.user.tierId))
            .leftJoin(QBadge.badge).on(QBadge.badge.id.eq(QUser.user.mainBadgeId))
            .where(QUser.user.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(userSimple);
    }

    @Override
    public List<Badge> findAllBadgeByUserId(Long userId) {
        return queryFactory
            .select(Projections.constructor(UserQueryResult.Badge.class,
                QBadge.badge.id,
                QBadge.badge.name,
                QBadge.badge.imgUrl,
                QBadge.badge.condition
            ))
            .from(QBadge.badge)
            .leftJoin(QUserBadge.userBadge).on(QUserBadge.userBadge.badgeId.eq(QBadge.badge.id))
            .where(QUserBadge.userBadge.userId.eq(userId))
            .fetch();
    }

}
