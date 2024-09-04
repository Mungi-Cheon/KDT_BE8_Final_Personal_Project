package com.fp.gatewayservice.filter;

import com.fp.gatewayservice.cookie.CookieProvider;
import com.fp.gatewayservice.exception.AuthException;
import com.fp.gatewayservice.exception.ErrorType;
import com.fp.gatewayservice.filter.AuthorizationFilter.Config;
import com.fp.gatewayservice.jwt.JwtProvider;
import com.fp.gatewayservice.jwt.TokenType;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<Config> {

    private final CookieProvider cookieProvider;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthorizationFilter(CookieProvider cookieProvider, JwtProvider jwtProvider) {
        super(Config.class);
        this.cookieProvider = cookieProvider;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String token = cookieProvider.getTokenFromCookies(request.getCookies());

            // Bearer token 형식으로 하는 것이 정석이나 view가 없기에 임의의 형태로 지정.
            jwtProvider.validateToken(token, TokenType.ACCESS);

            Date expiredDate = jwtProvider.getExpired(token, TokenType.ACCESS);
            if (expiredDate.before(new Date())) {
                throw new AuthException(ErrorType.TOKEN_EXPIRED);
            }

            Long memberId = jwtProvider.getMemberIdByToken(token, TokenType.ACCESS);

            ServerHttpRequest newRequest = request.mutate()
                .header("member-id", String.valueOf(memberId)).build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    public static class Config {

    }
}
