package video.jpa;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import video.Video;

public interface VideoJpaRepository extends JpaRepository<Video, Long> {

    @Query(
        value = "SELECT v FROM Video v WHERE v.placeId IS NULL AND v.influencerId = :influencerId",
        countQuery = "SELECT COUNT(v) FROM Video v where v.placeId IS NULL AND v.influencerId = :influencerId"
    )
    Page<Video> findAllByPlaceIsNullAndInfluencerId(Pageable pageable, Long influencerId);

    Boolean existsByUuid(String uuid);

    @Query(
        value = "SELECT v FROM Video v WHERE v.placeId IS NULL",
        countQuery = "SELECT COUNT(v) FROM Video v where v.placeId IS NULL"
    )
    Page<Video> findAllByPlaceIdIsNull(Pageable pageable);

    List<Video> findAllByDeleteAtIsNull();
}
