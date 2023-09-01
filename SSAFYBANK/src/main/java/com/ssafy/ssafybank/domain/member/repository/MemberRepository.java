package com.ssafy.ssafybank.domain.member.repository;

import com.ssafy.ssafybank.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
