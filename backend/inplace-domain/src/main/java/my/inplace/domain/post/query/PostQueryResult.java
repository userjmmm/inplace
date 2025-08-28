package my.inplace.domain.post.query;

import java.time.LocalDateTime;
import java.util.List;

public class PostQueryResult {

    public record DetailedPost(
        Long postId,
        String userNickname,
        String userImageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl,
        String title,
        String content,
        List<Image> imageInfos, // TODO : JsonNode -> List<Image> 반환으로 변경
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

    }

    public record CursorDetailedPost(
        DetailedPost detailedPost,
        Long cursorId
    ) {

    }

    public record UserSuggestion(
        Long userId,
        String userNickname,
        String userImageUrl
    ) {

    }

    public record Image(
        String imageUrl,
        String imageHash
    ) {

    }
}
