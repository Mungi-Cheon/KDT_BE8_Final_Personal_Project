package com.fp.gatewayservice.jwt;

import com.fp.gatewayservice.exception.AuthException;
import com.fp.gatewayservice.exception.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
    private String ACCESS_SECRET;
    @Value("${jwt.refresh-secret}")
    private String REFRESH_SECRET;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    public void init() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(ACCESS_SECRET);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(REFRESH_SECRET);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public void validateToken(String token, TokenType type) {
        Key key = type.equals(TokenType.ACCESS) ? accessKey : refreshKey;
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException ex) {
            throw new AuthException(ErrorType.TOKEN_AUTHORIZATION_FAIL);
        }

        validateTokenExpired(token, type);
    }

    private void validateTokenExpired(String token, TokenType type) {
        Date expiredDate = getExpired(token, type);
        if (expiredDate.before(new Date())) {
            throw new AuthException(ErrorType.TOKEN_EXPIRED);
        }
    }

    public Date getExpired(String token, TokenType type) {
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
