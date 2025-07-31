package team7.inplace.admin.banner.persistence;

import banner.Banner;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT l FROM Banner l WHERE l.startDate <= :now AND l.endDate >= :now or l.isFixed = true")
    List<Banner> findActiveBanner(@Param("now") LocalDateTime now);
}