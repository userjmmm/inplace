package post.query;

import java.util.List;
import java.util.Optional;
import team7.inplace.global.cursor.CursorResult;

public interface PostReadRepository {

    CursorResult<PostQueryResult.DetailedPost> findPostsOrderBy(
        Long userId,
        Long cursorId,
        int size,
        String orderBy
    );

    Optional<PostQueryResult.DetailedPost> findPostById(Long postId, Long userId);

    List<PostQueryResult.UserSuggestion> findCommentUserSuggestions(Long postId, String keyword);
}
