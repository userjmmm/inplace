package team7.inplace.post.persistence;

import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;

public interface PostReadRepository {

    CursorResult<DetailedPost> findPostsOrderByCreatedDate(
        Long userId,
        Long cursorId,
        int size,
        String orderBy
    );
}
