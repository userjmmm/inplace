package team7.inplace.token.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.AuthorizationErrorCode;
import team7.inplace.security.filter.TokenType;
import team7.inplace.security.util.AuthorizationUtil;
import team7.inplace.security.util.CookieUtil;
import team7.inplace.security.util.JwtUtil;
import team7.inplace.token.application.RefreshTokenFacade;
import team7.inplace.token.application.dto.TokenCommand.ReIssued;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController implements RefreshTokenControllerApiSpec {

    private final JwtUtil jwtUtil;
    private final RefreshTokenFacade refreshTokenFacade;

    @GetMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@CookieValue(value = "refresh_token") Cookie cookie,
        HttpServletResponse response) {
        if (cannotRefreshToken(cookie)) {
            throw InplaceException.of(AuthorizationErrorCode.INVALID_TOKEN);
        }

        String refreshToken = cookie.getValue();
        ReIssued reIssuedToken = refreshTokenFacade.getReIssuedRefreshTokenCookie(
            jwtUtil.getUsername(refreshToken), refreshToken);
        addTokenToCookie(response, reIssuedToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addTokenToCookie(HttpServletResponse response, ReIssued reIssuedToken) {
        ResponseCookie accessTokenCookie = CookieUtil.createCookie(
            TokenType.ACCESS_TOKEN.getValue(),
            reIssuedToken.accessToken());
        ResponseCookie refreshTokenCookie = CookieUtil.createCookie(
            TokenType.REFRESH_TOKEN.getValue(),
            reIssuedToken.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private boolean cannotRefreshToken(Cookie cookie) {
        return AuthorizationUtil.getUserId() == null || !jwtUtil.isRefreshToken(cookie.getValue());
    }

}
