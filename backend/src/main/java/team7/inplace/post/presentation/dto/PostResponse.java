package team7.inplace.post.presentation.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import team7.inplace.global.cursor.CursorResponse;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.persistence.dto.PostQueryResult;
import team7.inplace.user.presentation.dto.UserResponse;

public class PostResponse {

    public record DetailedList(
        List<DetailedPost> posts,
        CursorResponse cursor
    ) {

        public static DetailedList from(
            CursorResult<PostQueryResult.DetailedPost> postQueryResult
        ) {
            List<DetailedPost> posts = postQueryResult.value().stream()
                .map(DetailedPost::from)
                .toList();
            return new DetailedList(posts,
                new CursorResponse(postQueryResult.hasNext(), postQueryResult.nextCursorId()));
        }
    }

    public record DetailedPost(
        Long postId,
        UserResponse.Info author,
        String title,
        String content,
        List<String> photoUrls,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        String createdAt
    ) {

        public static DetailedPost from(PostQueryResult.DetailedPost postQueryResult) {
            return new DetailedPost(
                postQueryResult.postId(),
                new UserResponse.Info(postQueryResult.userNickname(),
                    postQueryResult.userImageUrl()),
                postQueryResult.title(),
                postQueryResult.content(),
                postQueryResult.getImageUrls(),
                postQueryResult.selfLike(),
                postQueryResult.totalLikeCount(),
                postQueryResult.totalCommentCount(),
                postQueryResult.isMine(),
                formatCreatedAt(postQueryResult.createdAt())
            );
        }

        private static String formatCreatedAt(LocalDateTime createdAt) {
            var now = LocalDateTime.now();
            var duration = Duration.between(createdAt, now);

            long minutes = duration.toMinutes();
            long hours = duration.toHours();
            long days = duration.toDays();

            if (minutes < 1) {
                return "방금 전";
            }
            if (minutes < 60) {
                return minutes + "분 전";
            }
            if (hours < 24) {
                return hours + "시간 전";
            }
            if (days < 7) {
                return days + "일 전";
            }
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }
}
