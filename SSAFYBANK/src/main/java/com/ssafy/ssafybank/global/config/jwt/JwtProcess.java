package com.ssafy.ssafybank.global.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

public class JwtProcess {
    private final Logger log = LoggerFactory.getLogger(getClass());


    // jwt 액세스 토큰 생성

    public static String createAccessToken(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("ssafybank")//토큰의 제목
                //1시간
               // .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))//만료시간= 헌재시간 + 유효기간
                .withClaim("memberUuid", loginUser.getMember().getMemberUuid())///나중에 유유아이디로 바꾸자
                .withClaim("role", loginUser.getMember().getRole() + "")//String이 되기 위해 + "" 붙여 주는 것 string이 들어와야 함
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        System.out.println(JwtVO.TOKEN_PREFIX + jwtToken);
        return jwtToken;
    }

    public static String reissueAccessToken(String uuid , String role) {
        String jwtToken = JWT.create()
                .withSubject("ssafybank")//토큰의 제목
                //1시간
                // .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))//만료시간= 헌재시간 + 유효기간
                .withClaim("memberUuid", uuid)///나중에 유유아이디로 바꾸자
                .withClaim("role", role + "")//String이 되기 위해 + "" 붙여 주는 것 string이 들어와야 함
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        System.out.println(JwtVO.TOKEN_PREFIX + jwtToken);
        return jwtToken;
    }


    // 액세스 토큰 검증 (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static LoginUser verifyAccessToken(String token) {
        //받아온 토큰을 디코드 하고 검증하기
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        String memberUuid = decodedJWT.getClaim("memberUuid").asString(); //토큰 내부는 암호화 안되어 있으니까 최소한의 정보만 넣자
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().memberUuid(memberUuid).role(role).build();//user이넘 타입으로 바뀌어서 들어감
        //실제 role에는 "CUSTOMER" 이런식으로 저장 되어 있었을 것임
        LoginUser loginUser = new LoginUser(member);

        return loginUser;
    }

}