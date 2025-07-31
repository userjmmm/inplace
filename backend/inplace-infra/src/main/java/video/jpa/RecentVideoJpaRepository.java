package video.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import video.RecentVideo;

public interface RecentVideoJpaRepository extends JpaRepository<RecentVideo, Long> {

}
