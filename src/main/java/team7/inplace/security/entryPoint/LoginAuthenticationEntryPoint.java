package team7.inplace.security.entryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class LoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${spring.redirect.front-end-url}")
    private String frontEndUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendRedirect(frontEndUrl);
    }
}
