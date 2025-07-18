package team7.inplace.alarm.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.alarm.domain.Alarm;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAlarmsByUserIdAndIsProcess(Long userId, Boolean isProcess);
    
    List<Alarm> findByUserId(Long userId);
}
