package team7.inplace.liked.likedComment.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import post.LikedComment;

public interface LikedCommentRepository extends JpaRepository<LikedComment, Long> {

    Optional<LikedComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
