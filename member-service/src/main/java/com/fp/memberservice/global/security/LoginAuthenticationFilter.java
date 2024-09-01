package com.fp.memberservice.global.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.memberservice.domain.dto.request.LoginRequest;
import com.fp.memberservice.global.cookie.CookieProvider;
import com.fp.memberservice.global.exception.AuthException;
import com.fp.memberservice.global.exception.ErrorType;
import com.fp.memberservice.global.security.jwt.JwtProvider;
import com.fp.memberservice.global.security.jwt.TokenType;
import com.fp.memberservice.global.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final CookieProvider cookieProvider;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        Authentication authentication;
        try {
            LoginRequest credential = objectMapper.readValue(request.getInputStream(),
                LoginRequest.class);

            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credential.getEmail(),
                    credential.getPassword()));
        } catch (IOException ex) {
            throw new AuthException(ErrorType.AUTHORIZATION_FAIL);
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        UserDetails user = (UserDetails) authResult.getPrincipal();
        Long memberId = Long.parseLong(user.getUsername());

        String accessToken = jwtProvider.createJwtToken(memberId, TokenType.ACCESS,
            request.getRequestURI());
        String refreshToken = tokenService.getRefreshToken(memberId);

        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = jwtProvider.createJwtToken(memberId, TokenType.REFRESH,
                request.getRequestURI());
            tokenService.saveRefreshToken(memberId, refreshToken);
        } else {
            tokenService.updateRefreshToken(memberId, refreshToken);
        }

        Date expiredTime = jwtProvider.getExpiredTime(accessToken, TokenType.ACCESS);

        // 쿠키 설정
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        Cookie cookie = cookieProvider.of(refreshTokenCookie);
        response.addCookie(cookie);

        // responseBody 설정
        Map<String, Object> tokens = Map.of(
            "accessToken", accessToken,
            "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        var responseResult = ResponseEntity.ok(tokens);
        String responseBody = objectMapper.writeValueAsString(responseResult);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(responseBody);
        response.getWriter().flush();
    }
}
