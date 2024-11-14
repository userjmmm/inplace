package team7.inplace.admin.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team7.inplace.admin.domain.Logo;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {
}
