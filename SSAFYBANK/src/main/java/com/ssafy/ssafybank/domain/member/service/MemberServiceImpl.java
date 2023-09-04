package com.ssafy.ssafybank.domain.member.service;

import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.config.jwt.JwtProcess;
import com.ssafy.ssafybank.global.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Transactional
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
    @Transactional
    @Override
    public String reissue(LoginUser loginUser) {
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
            throw new CustomApiException("accessToken정보가 잘못되었습니다.");
        }

    }
}
