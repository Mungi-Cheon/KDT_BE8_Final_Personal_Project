package com.fp.memberservice.global.service;

import com.fp.memberservice.domain.exception.ErrorType;
import com.fp.memberservice.domain.exception.MemberException;
import com.fp.memberservice.domain.repository.MemberRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(String.valueOf(memberId), refreshToken, Duration.ofDays(7));
    }

    public String getRefreshToken(Long memberId) {
        return (String) redisTemplate.opsForValue().get(String.valueOf(memberId));
    }

    public void removeToken(Long memberId) {
        redisTemplate.delete(String.valueOf(memberId));
    }

    public void updateRefreshToken(Long memberId, String refreshToken) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(ErrorType.NOT_FOUND));
        redisTemplate.opsForValue().set(String.valueOf(memberId), refreshToken, Duration.ofDays(7));
    }
}
