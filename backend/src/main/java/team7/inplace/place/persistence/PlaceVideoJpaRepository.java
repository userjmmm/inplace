package team7.inplace.place.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import place.PlaceVideo;

@Repository
public interface PlaceVideoJpaRepository extends JpaRepository<PlaceVideo, Long> {

}
