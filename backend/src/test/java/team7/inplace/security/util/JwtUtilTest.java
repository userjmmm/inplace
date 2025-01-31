package team7.inplace.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team7.inplace.security.config.JwtProperties;
import team7.inplace.user.domain.Role;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JwtUtilTest {

    String testSecretKey = "testKeyTestKeyTestKeyTestKeyTestKeyTestKey";
    JwtProperties jwtProperties;
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties(testSecretKey, 60000L,
                180000L);
        jwtUtil = new JwtUtil(jwtProperties);
    }

    @Test
    void createTokenTest() {
        String username = "testMan";
        Long userId = 3L;
        String accessToken = jwtUtil.createAccessToken(username, userId, Role.USER.getRoles());
        assertThat(accessToken).isNotNull();
    }

    @Test
    void createRefreshTokenTest() {
        String username = "testMan";
        Long userId = 3L;
        String refreshToken = jwtUtil.createRefreshToken(username, userId, Role.USER.getRoles());
        assertThat(refreshToken).isNotNull();
    }

    @Test
    void getInfoTests() {
        String username = "testMan";
        Long userId = 3L;
        String accessToken = jwtUtil.createAccessToken(username, userId, Role.USER.getRoles());
        String refreshToken = jwtUtil.createRefreshToken(username, userId, Role.USER.getRoles());

        assertAll(
                () -> assertThat(jwtUtil.getUsername(accessToken)).isEqualTo(username),
                () -> assertThat(jwtUtil.getId(accessToken)).isEqualTo(userId),
                () -> assertThat(jwtUtil.getRoles(accessToken)).isEqualTo(Role.USER.getRoles()),
                () -> assertThat(jwtUtil.getUsername(refreshToken)).isEqualTo(username),
                () -> assertThat(jwtUtil.getId(refreshToken)).isEqualTo(userId),
                () -> assertThat(jwtUtil.getRoles(refreshToken)).isEqualTo(Role.USER.getRoles())
        );
    }

    @Test
    void 토큰이_만료된_경우() {
        Date expiredDate = new Date(System.currentTimeMillis() - 3600 * 1000);
        SecretKey secretKey = new SecretKeySpec(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8),
                SIG.HS256.key().build().getAlgorithm());
        String expiredToken = Jwts.builder()
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();

        assertThat(jwtUtil.isNotExpired(expiredToken)).isFalse();
    }
}
