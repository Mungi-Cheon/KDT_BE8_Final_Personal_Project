package com.fp.memberservice.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fp.memberservice.global.cookie.CookieProvider;
import com.fp.memberservice.global.security.LoginAuthenticationFilter;
import com.fp.memberservice.global.security.entrypoint.CustomAuthenticationEntryPoint;
import com.fp.memberservice.global.security.jwt.JwtProvider;
import com.fp.memberservice.global.security.jwt.TokenType;
import com.fp.memberservice.global.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final CookieProvider cookieProvider;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager()
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(
            objectMapper, authenticationManager(), jwtProvider, cookieProvider,
            tokenService);
        loginAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers ->
                headers.frameOptions(FrameOptionsConfig::disable))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(
                config -> config.authenticationEntryPoint(customAuthenticationEntryPoint))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/login", "/api/auth/signup").permitAll()
                    .requestMatchers("favicon.ico", "/error").permitAll()
                    .anyRequest().authenticated())
            .logout(out -> out.logoutUrl("/api/logout").deleteCookies(TokenType.REFRESH.getName()))
            .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
