package team7.inplace.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Value("${spring.redirect.front-end-url}")
    private String frontEndUrl;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        log.info("Access denied");
        response.sendRedirect(frontEndUrl);
    }
}
