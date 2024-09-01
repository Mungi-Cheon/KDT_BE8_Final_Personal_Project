package com.fp.memberservice.domain.repository;

import com.fp.memberservice.domain.entity.Member;
import com.fp.memberservice.domain.exception.ErrorType;
import com.fp.memberservice.domain.exception.MemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Query(value = "delete from Member where email = :email ")
    void deleteByEmail(String email);

    default Member getMember(Long memberId) {
        return findById(memberId).orElseThrow(
            () -> new MemberException(ErrorType.NOT_FOUND));
    }
}
