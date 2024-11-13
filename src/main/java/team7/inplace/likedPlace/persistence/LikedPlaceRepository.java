package team7.inplace.likedPlace.persistence;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team7.inplace.likedPlace.domain.LikedPlace;

public interface LikedPlaceRepository extends JpaRepository<LikedPlace, Long> {

    Optional<LikedPlace> findByUserIdAndPlaceId(Long userId, Long placeId);

    Page<LikedPlace> findByUserIdAndIsLikedTrue(Long userId, Pageable pageable);

    @Query("SELECT l.place.id FROM LikedPlace l WHERE l.user.id = :userId AND l.isLiked = true")
    Set<Long> findPlaceIdsByUserIdAndIsLikedTrue(@Param("userId") Long userId);
}
