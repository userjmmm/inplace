package my.inplace.infra.place.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import my.inplace.domain.place.Place;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {

    Optional<Place> findPlaceByKakaoPlaceId(Long placeId);

    @Query("SELECT p.googlePlaceId FROM places p WHERE p.id = :placeId")
    Optional<String> findGooglePlaceIdById(Long placeId);
}
