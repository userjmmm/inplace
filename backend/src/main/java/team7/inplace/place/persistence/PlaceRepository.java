package team7.inplace.place.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.inplace.place.domain.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceCustomRepository {

}
