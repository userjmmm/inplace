package place.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import place.PlaceVideo;

@Repository
public interface PlaceVideoJpaRepository extends JpaRepository<PlaceVideo, Long> {

}
