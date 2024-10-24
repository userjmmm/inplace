package team7.inplace.global.exception;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String errorUrl;
    private String errorMessage;
    private String stackTrace;

    private ErrorLog(String errorUrl, String errorMessage, String stackTrace) {
        this.errorUrl = errorUrl;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public static ErrorLog of(String url, String message, String stackTrace) {
        return new ErrorLog(url, message, stackTrace);
    }
}
