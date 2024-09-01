package com.fp.memberservice.global.cookie;

import static com.fp.memberservice.global.security.jwt.TokenType.REFRESH;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    @Value("${jwt.refresh-token-expired-time}")
    Long REFRESH_EXPIRED_TIME;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie
            .from(REFRESH.getName(), refreshToken)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(REFRESH_EXPIRED_TIME).build();
    }

    public ResponseCookie removeTokenCookie() {
        return ResponseCookie
            .from(REFRESH.getName(), null)
            .path("/")
            .maxAge(0)
            .build();
    }

    public Cookie of(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        return cookie;
    }
}
