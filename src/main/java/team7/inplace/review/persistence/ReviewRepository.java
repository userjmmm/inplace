package team7.inplace.review.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team7.inplace.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    Page<Review> findByPlaceId(Long placeId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN FETCH r.place WHERE r.user.id = :userId")
    Page<Review> findByUserIdWithPlace(Long userId, Pageable pageable);
}
