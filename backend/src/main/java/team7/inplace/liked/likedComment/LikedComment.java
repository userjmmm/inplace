package team7.inplace.liked.likedComment;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.baseEntity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedComment extends BaseEntity {

    private Long userId;
    private Long commentId;

    public LikedComment(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
