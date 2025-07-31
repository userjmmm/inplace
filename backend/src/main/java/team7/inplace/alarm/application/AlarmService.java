package team7.inplace.alarm.application;

import alarm.Alarm;
import alarm.AlarmRepository;
import alarm.AlarmType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.alarm.application.dto.AlarmInfo;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

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
