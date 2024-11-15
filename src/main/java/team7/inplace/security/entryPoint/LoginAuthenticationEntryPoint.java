package team7.inplace.security.entryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;

public class LoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public LoginAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        setErrorResponse(response, InplaceException.of(AuthorizationErrorCode.NOT_AUTHENTICATION));
    }

    private void setErrorResponse(
        HttpServletResponse response,
        InplaceException inplaceException
    ) throws IOException {
        response.setStatus(inplaceException.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            inplaceException.getHttpStatus(), inplaceException.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}
