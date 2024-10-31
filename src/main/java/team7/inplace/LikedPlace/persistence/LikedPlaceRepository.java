package team7.inplace.LikedPlace.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.LikedPlace.domain.LikedPlace;

public interface LikedPlaceRepository extends JpaRepository<LikedPlace, Long> {

}
