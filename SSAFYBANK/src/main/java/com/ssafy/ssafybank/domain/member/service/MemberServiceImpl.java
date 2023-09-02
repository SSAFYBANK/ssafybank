package com.ssafy.ssafybank.domain.member.service;

import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public Member join(String kakaoId) {
        String memberUuid = UUID.randomUUID().toString();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("");
        String role = "USER";
        Member member = Member.builder().
                memberUuid(memberUuid)
                .status(false)
                .password(password)
                .kakaoId(kakaoId)
                .role(role)
                .build();
        Member savedMamber = memberRepository.save(member);

        return savedMamber;
    }
}
