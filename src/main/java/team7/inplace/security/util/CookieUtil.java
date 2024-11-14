package team7.inplace.security.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie createCookie(String key, String value) {
        return ResponseCookie.from(key, value)
              .sameSite("None")
              .secure(true)
              .path("/")
              .httpOnly(true)
              .maxAge(60 * 60)
              .build();
    }
}
