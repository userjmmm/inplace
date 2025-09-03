package my.inplace.infra.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.user.QBadge;
import my.inplace.domain.user.QTier;
import my.inplace.domain.user.QUser;
import my.inplace.domain.user.QUserBadge;
import my.inplace.domain.user.query.UserQueryResult;
import my.inplace.domain.user.query.UserQueryResult.Badge;
import my.inplace.domain.user.query.UserQueryResult.Simple;
import my.inplace.domain.user.query.UserReadRepository;
import org.springframework.stereotype.Repository;

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
