package my.inplace.domain.post;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "liked_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedComment extends BaseEntity {

    private Long userId;
    private Long commentId;
    private Boolean isLiked;

    private LikedComment(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
        this.isLiked = false;
    }

    public static LikedComment from(Long userId, Long commentId) {
        return new LikedComment(userId, commentId);
    }

    public boolean updateLike(boolean like) {
        return this.isLiked = like;
    }
}
