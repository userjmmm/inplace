package team7.inplace.alarm.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import team7.inplace.alarm.event.AlarmEvent.PostReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.CommentReportAlarmEvent;
import team7.inplace.alarm.event.AlarmEvent.MentionAlarmEvent;

@Component
@RequiredArgsConstructor
public class AlarmEventHandler {
    
    // todo 쓰레드 풀 만들기
    @Async
    @EventListener
    public void processMentionAlarm(MentionAlarmEvent mentionAlarmEvent) {
        // todo 통신 로직 정하고 구현하기
    }
    
    @Async
    @EventListener
    public void processPostReportAlarm(PostReportAlarmEvent postReportAlarmEvent) {
        // todo 통신 로직 정하고 구현하기
    }
    
    @Async
    @EventListener
    public void processPostReportAlarm(CommentReportAlarmEvent commentReportAlarmEvent) {
        // todo 통신 로직 정하고 구현하기
    }
}
