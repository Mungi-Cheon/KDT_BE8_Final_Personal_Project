package com.fp.gatewayservice.cookie;

import com.fp.gatewayservice.jwt.TokenType;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class CookieProvider {

    public String getTokenFromCookies(MultiValueMap<String, HttpCookie> cookies) {
        if (cookies.isEmpty()) {
            return null;
        }
        return cookies.get(TokenType.ACCESS.getName()).stream().map(HttpCookie::getValue)
            .findFirst()
            .orElse(null);
    }
}
