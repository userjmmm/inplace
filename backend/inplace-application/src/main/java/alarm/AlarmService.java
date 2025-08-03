package alarm;

import alarm.dto.AlarmInfo;
import alarm.jpa.AlarmJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmJpaRepository alarmJpaRepository;

    @Transactional
    public void saveAlarm(
        Long userId, Long postId, Long commentId, String content, AlarmType alarmType) {
        Alarm alarm = new Alarm(userId, postId, commentId, content, alarmType);

        alarmJpaRepository.save(alarm);
    }

    @Transactional(readOnly = true)
    public List<AlarmInfo> getAlarmInfos(Long userId) {
        return alarmJpaRepository.findByUserId(userId).stream()
            .map(AlarmInfo::from)
            .toList();
    }

    @Transactional
    public void checkAlarm(Long id) {
        Alarm alarm = alarmJpaRepository.findById(id).orElseThrow();
        alarm.checked();
    }
}
