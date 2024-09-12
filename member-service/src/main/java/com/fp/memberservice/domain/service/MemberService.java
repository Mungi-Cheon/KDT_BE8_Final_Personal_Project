package com.fp.memberservice.domain.service;


import com.fp.memberservice.domain.dto.request.SignupRequest;
import com.fp.memberservice.domain.dto.response.MemberResponse;
import com.fp.memberservice.domain.entity.Member;
import com.fp.memberservice.domain.exception.ErrorType;
import com.fp.memberservice.domain.exception.MemberException;
import com.fp.memberservice.domain.repository.MemberRepository;
import com.fp.memberservice.global.cookie.CookieProvider;
import com.fp.memberservice.global.security.jwt.JwtProvider;
import com.fp.memberservice.global.security.jwt.TokenType;
import com.fp.memberservice.global.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Transactional
    public MemberResponse signup(SignupRequest request) {
        String email = request.getEmail();

        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(ErrorType.DUPLICATED_MEMBER);
        }

        String password = passwordEncoder.encode(request.getPassword());
        Member newMember = Member.from(email, request.getName(), password);

        Member savedMember = memberRepository.save(newMember);
        return MemberResponse.from(savedMember);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = Objects.requireNonNull(
            CookieProvider.getCookie(request, TokenType.ACCESS.getName())).getValue();

        Long memberId = jwtProvider.getMemberIdByToken(accessToken, TokenType.ACCESS);
        memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(ErrorType.NOT_FOUND));

        tokenService.addBlackList(accessToken);
        CookieProvider.removeCookie(response, TokenType.REFRESH.getName());
        CookieProvider.removeCookie(response, TokenType.ACCESS.getName());
    }


    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD));

        tokenService.removeToken(member.getId());
        memberRepository.delete(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
            .orElseThrow(() -> new MemberException(ErrorType.NOT_FOUND));
    }
}

