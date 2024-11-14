package team7.inplace.admin.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team7.inplace.admin.domain.Logo;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {
    @Query("SELECT l FROM Logo l WHERE l.startDate <= :now AND l.endDate >= :now or l.isFixed = true")
    List<Logo> findActiveLogos(@Param("now") LocalDateTime now);
}