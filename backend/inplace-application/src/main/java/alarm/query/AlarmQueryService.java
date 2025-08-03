package alarm.query;

import alarm.dto.AlarmInfo;
import alarm.jpa.AlarmJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmQueryService {

    private final AlarmJpaRepository alarmJpaRepository;

    @Transactional(readOnly = true)
    public List<AlarmInfo> getAlarmInfos(Long userId) {
        return alarmJpaRepository.findByUserId(userId).stream()
            .map(AlarmInfo::from)
            .toList();
    }
}
