package alarm;

import alarm.dto.AlarmInfo;
import annotation.Facade;
import lombok.RequiredArgsConstructor;

import team7.inplace.security.util.AuthorizationUtil;
import user.UserService;

import java.util.List;

@Facade
@RequiredArgsConstructor
public class AlarmFacade {
    private final UserService userService;
    private final AlarmService alarmService;
    
    public void updateFcmToken(String token) {
        var userId = AuthorizationUtil.getUserIdOrThrow(); // TODO - security module
        
        userService.updateFcmToken(userId, token);
    }
    
    public List<AlarmInfo> getAlarmInfos() {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        
        return alarmService.getAlarmInfos(userId);
    }
    
    public void processAlarm(Long id) {
        alarmService.readAlarm(id);
    }
}
