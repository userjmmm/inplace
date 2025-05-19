package team7.inplace.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum AuthorizationErrorCode implements ErrorCode {
    NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "A000", "Authentication failed"),
    TOKEN_IS_EMPTY(HttpStatus.BAD_REQUEST, "A001", "not include token"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "A002", "Invalid token"),
    TOKEN_IS_EXPIRED(HttpStatus.BAD_REQUEST, "A003", "token is expired");

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
