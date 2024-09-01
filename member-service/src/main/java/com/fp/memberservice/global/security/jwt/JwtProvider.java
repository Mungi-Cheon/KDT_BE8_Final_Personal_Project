package com.fp.memberservice.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.access-secret}")
    String ACCESS_SECRET;
    @Value("${jwt.refresh-secret}")
    String REFRESH_SECRET;
    @Value("${jwt.issuer}")
    String ISSUER;
    @Value("${jwt.access-token-expired-time}")
    Long ACCESS_EXPIRED_TIME;
    @Value("${jwt.refresh-token-expired-time}")
    Long REFRESH_EXPIRED_TIME;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(ACCESS_SECRET);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(REFRESH_SECRET);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String createJwtToken(Long memberId, TokenType type, String requestURI) {

        Key key;
        long expirationTime;
        if (type.equals(TokenType.ACCESS)) {
            key = accessKey;
            expirationTime = ACCESS_EXPIRED_TIME;
        } else {
            key = refreshKey;
            expirationTime = REFRESH_EXPIRED_TIME;
        }

        return Jwts.builder()
            .claim("category", "access")
            .claim("memberId", memberId)
            .setIssuer(ISSUER + "_" + requestURI)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Date getExpiredTime(String token, TokenType type) {
        return getClaimsFromJwtToken(token, type).getExpiration();
    }

    public Long getMemberIdByToken(String token, TokenType type) {
        return getClaimsFromJwtToken(token, type).get("memberId", Long.class);
    }

    private Claims getClaimsFromJwtToken(String token, TokenType type) {
        Key key = type.equals(TokenType.ACCESS) ? accessKey : refreshKey;
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
