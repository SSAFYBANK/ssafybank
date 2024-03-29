package com.ssafy.ssafybank.domain.member.repository;

import com.ssafy.ssafybank.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberUuid(String memberUuid);

    Optional<Member> findByKakaoIdAndStatus(String kakaoId, boolean status);
}
