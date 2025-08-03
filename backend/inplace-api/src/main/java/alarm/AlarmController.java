package alarm;

import alarm.dto.AlarmRequest;
import alarm.dto.AlarmResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import team7.inplace.alarm.application.AlarmFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmFacade alarmFacade;

    // 로그인 순간에 FCM 토큰 받기
    @PostMapping
    public ResponseEntity<Void> upsertFcmToken(
        @RequestBody AlarmRequest alarmRequest
    ) {
        alarmFacade.updateFcmToken(alarmRequest.token());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AlarmResponse>> getAlarmList() {
        var response = alarmFacade.getAlarmInfos().stream()
            .map(AlarmResponse::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> processOneAlarm(
        @PathVariable Long id
    ) {
        alarmFacade.processAlarm(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
