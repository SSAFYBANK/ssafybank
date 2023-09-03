package com.ssafy.ssafybank.global.config.auth;


import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;

import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import com.ssafy.ssafybank.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private KakaoLoginService kakaoLoginService;
    @Autowired
    private MemberService memberService;

    @Override
    //username만 들어옴
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        String accessToken = kakaoLoginService.getKakaoAccessToken(code);
        String kakaoId = kakaoLoginService.createKakaoUser(accessToken);
        if(kakaoId.equals("") || kakaoId.equals(null)){
            throw new InternalAuthenticationServiceException("로그인실패");
        }
        System.out.println("kakaoId: " + kakaoId);
        System.out.println("loginService");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.printf("password" + passwordEncoder.encode(""));
        //더미데이터들어가있음
        Optional<Member> userPS = memberRepository.findByKakaoIdAndStatus(kakaoId, false);
        if (!userPS.isPresent()) {
            Member newMember = memberService.join(kakaoId);
            if (newMember == null) {
                throw new InternalAuthenticationServiceException("인증 및 가입 실패");
            }
            return new LoginUser(newMember);
        } else {
            return new LoginUser(userPS.get());
        }


    }

}