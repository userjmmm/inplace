package user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import user.Tier;

public interface UserTierJpaRepository extends JpaRepository<Tier, Long> {

}
