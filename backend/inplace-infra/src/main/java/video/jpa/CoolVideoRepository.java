package video.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import video.CoolVideo;

public interface CoolVideoRepository extends JpaRepository<CoolVideo, Long> {

    List<CoolVideo> findByPlaceCategoryParentName(String parentCategoryName);
}
