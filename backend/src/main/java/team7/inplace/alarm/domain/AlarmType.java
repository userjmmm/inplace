package team7.inplace.alarm.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlarmType {
    MENTION("번 게시물에서 회원님을 언급했습니다!"),
    REPORT("이 신고로 인하여 삭제되었습니다!");
    
    private final String content;
    
    public String content() {
        return this.content;
    }
}
