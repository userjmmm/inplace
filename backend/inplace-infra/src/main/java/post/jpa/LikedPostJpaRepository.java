package post.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import post.LikedPost;

@Repository
public interface LikedPostJpaRepository extends JpaRepository<LikedPost, Long> {

    Optional<LikedPost> findByUserIdAndPostId(Long userId, Long postId);
}
