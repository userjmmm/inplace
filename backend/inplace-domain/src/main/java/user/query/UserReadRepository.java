package user.query;

import java.util.List;
import java.util.Optional;
import user.query.UserQueryResult.Badge;
import user.query.UserQueryResult.Simple;

public interface UserReadRepository {

    Optional<Simple> findUserInfoById(Long id);

    List<Badge> findAllBadgeByUserId(Long userId);
}
