package team7.inplace.global.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "R001", "place에 대한 리뷰가 이미 존재합니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "리뷰를 찾을 수 없습니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, "R003", "본인이 작성한 리뷰만 삭제할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
