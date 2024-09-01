package com.fp.memberservice.global.cookie;

import com.fp.memberservice.global.security.jwt.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    @Value("${jwt.access-token-expired-time}")
    Long ACCESS_EXPIRED_TIME;

    @Value("${jwt.refresh-token-expired-time}")
    Long REFRESH_EXPIRED_TIME;

    public Cookie createCookie(String token, TokenType type) {
        Long maxAge = type.equals(TokenType.ACCESS) ? ACCESS_EXPIRED_TIME : REFRESH_EXPIRED_TIME;
        ResponseCookie responseCookie = ResponseCookie
            .from(type.getName(), token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(maxAge).build();
        return of(responseCookie);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        ResponseCookie responseCookie = ResponseCookie.from(name, StringUtils.EMPTY)
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader("Set-Cookie", responseCookie.toString());
    }

    private Cookie of(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        return cookie;
    }
}
