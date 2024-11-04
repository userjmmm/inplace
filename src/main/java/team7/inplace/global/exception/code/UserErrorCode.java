package team7.inplace.global.exception.code;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User is not found"),
    OAUTH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "OauthToken is not found");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return status;
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
