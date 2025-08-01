package alarm.jpa;

import java.util.List;

import alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUserId(Long userId);
}
