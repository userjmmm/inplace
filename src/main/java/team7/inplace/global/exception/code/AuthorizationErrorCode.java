package team7.inplace.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthorizationErrorCode implements ErrorCode {
    NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "A000", "Authentication 실패"),
    TOKEN_IS_EMPTY(HttpStatus.BAD_REQUEST, "A001", "토큰 없음"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "A002", "Invalid 토큰"),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST, "A003", "토큰 만료");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return errorCode;
    }

    @Override
    public String message() {
        return message;
    }
}
