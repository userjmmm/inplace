package review.dto;

import java.time.LocalDate;
import review.Review;
import review.query.ReviewQueryResult;

public class ReviewResult {

    public record Detail(
        Long id,
        boolean likes,
        String comment,
        Long userId,
        LocalDate createdDate,
        boolean mine
    ) {

        public static Detail from(Review review, boolean isMine) {
            return new Detail(
                review.getId(),
                review.getIsLiked(),
                review.getComment(),
                review.getUserId(),
                review.getCreatedDate(),
                isMine
            );
        }
    }

    public record Simple(
        Long reviewId,
        Boolean likes,
        String comment,
        String userNickname,
        LocalDate createdAt,
        Boolean mine
    ) {
        public static Simple from(ReviewQueryResult.Simple review) {
            return new Simple(
                review.reviewId(),
                review.likes(),
                review.comment(),
                review.userNickname(),
                review.createdAt(),
                review.mine()
            );
        }
    }

    public record LikeRate(
        Long likes,
        Long dislikes
    ) {

        public static LikeRate from(ReviewQueryResult.LikeRate review) {
            return new LikeRate(
                review.likes(),
                review.dislikes()
            );
        }
    }
}
