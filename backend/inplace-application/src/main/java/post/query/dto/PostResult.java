package post.query.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import post.Comment;
import post.Post;

public class PostResult {

    public record PostImages(
        List<PostImage> images
    ) {

        public static PostImages from(Post post) {
            List<PostImage> images = new ArrayList<>();
            for (int i = 0; i < post.getImageUrls().size(); i++) {
                images.add(new PostImage(post.getImageUrls().get(i), post.getImgHashes().get(i)));
            }
            return new PostImages(images);
        }
    }

    public record PostImage(
        String imageUrl,
        String imageHash
    ) {

    }

    public record ReportedPost(
        Long id,
        Long authorId,
        String content,
        Boolean isReported,
        LocalDateTime deleteAt
    ) {

        public static ReportedPost from(Post post) {
            return new ReportedPost(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                post.getIsReported(),
                post.getDeleteAt()
            );
        }
    }

    public record ReportedComment(
        Long id,
        Long authorId,
        String content,
        Boolean isReported,
        LocalDateTime deleteAt
    ) {

        public static ReportedComment from(Comment comment) {
            return new ReportedComment(
                comment.getId(),
                comment.getAuthorId(),
                comment.getContent(),
                comment.getIsReported(),
                comment.getDeleteAt()
            );
        }

    }
}
