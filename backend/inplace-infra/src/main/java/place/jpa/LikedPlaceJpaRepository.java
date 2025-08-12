package place.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import place.LikedPlace;

public interface LikedPlaceJpaRepository extends JpaRepository<LikedPlace, Long> {

    @Query("SELECT lp FROM LikedPlace lp WHERE lp.userId = :userId AND lp.placeId = :placeId AND lp.deleteAt IS NULL")
    Optional<LikedPlace> findByUserIdAndPlaceId(Long userId, Long placeId);
}
