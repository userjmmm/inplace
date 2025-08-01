package alarm;

import alarm.Alarm;
import alarm.AlarmType;
import java.util.List;

import alarm.dto.AlarmInfo;
import alarm.jpa.AlarmJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmJpaRepository alarmRepository;

    @Transactional
    public void saveAlarm(
        Long userId, Long postId, Long commentId, String content, AlarmType alarmType) {
        Alarm alarm = new Alarm(userId, postId, commentId, content, alarmType);

        alarmRepository.save(alarm);
    }

    @Transactional(readOnly = true)
    public List<AlarmInfo> getAlarmInfos(Long userId) {
        return alarmRepository.findByUserId(userId).stream()
            .map(AlarmInfo::from)
            .toList();
    }

    @Transactional
    public void readAlarm(Long id) {
        Alarm alarm = alarmRepository.findById(id).orElseThrow();
        alarm.checked();
    }
}
