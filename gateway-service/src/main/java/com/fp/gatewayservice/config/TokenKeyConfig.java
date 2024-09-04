package com.fp.gatewayservice.config;

import com.fp.gatewayservice.jwt.TokenType;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class TokenKeyConfig {

    @Bean
    public KeyResolver tokenKeyResolver() {
        return exchange -> {
            var cookies = exchange.getRequest().getCookies().getFirst(TokenType.ACCESS.getName());
            if (cookies != null) {
                return Mono.just(cookies.getValue());
            } else {
                String clientIp = Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                    .getAddress()
                    .getHostAddress();
                String hashedIp = DigestUtils.sha256Hex(clientIp);
                return Mono.just(hashedIp);
            }
        };
    }
}
