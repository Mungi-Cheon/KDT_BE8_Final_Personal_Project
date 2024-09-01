package com.fp.memberservice.domain.dto.response;

import com.fp.memberservice.domain.entity.Member;
import lombok.Builder;

@Builder
public record MemberResponse(

    Long id, String email, String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getName());
    }
}
