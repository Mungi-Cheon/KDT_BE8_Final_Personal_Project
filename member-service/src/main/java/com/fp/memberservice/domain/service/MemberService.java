package com.fp.memberservice.domain.service;


import com.fp.memberservice.domain.dto.request.SignupRequest;
import com.fp.memberservice.domain.dto.response.MemberResponse;
import com.fp.memberservice.domain.entity.Member;
import com.fp.memberservice.domain.exception.ErrorType;
import com.fp.memberservice.domain.exception.MemberException;
import com.fp.memberservice.domain.repository.MemberRepository;
import com.fp.memberservice.global.service.TokenService;
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

