package com.ssafy.ssafybank.domain.member.service;

import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.config.jwt.JwtProcess;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Member join(String kakaoId) {
        log.info("MemberServiceImpl_join");
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
        log.info("MemberServiceImpl_join : {}",kakaoId);
        return savedMamber;
    }
    @Transactional
    @Override
    public String reissue(LoginUser loginUser) {
        log.info("MemberServiceImpl_reissue");
        String memberUuid = loginUser.getMember().getMemberUuid();
        Optional<Member> memberOptional = memberRepository.findByMemberUuid(memberUuid);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String newUuid = UUID.randomUUID().toString();
            member.updateUuid(newUuid);
            String JWT = JwtProcess.reissueAccessToken(newUuid , "USER");
            return JWT;
        }
        else {
            log.error("MemberServiceImpl_reissue -> accessToken 정보가 잘못되었습니다.");
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }
}
