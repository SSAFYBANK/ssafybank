package com.ssafy.ssafybank.global.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ssafy.ssafybank.domain.member.dto.request.LoginReqDto;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//로그인 인증 필터임 securityconfig에서 따로 등록이 필요함
//UsernamePasswordAuthenticationFilter 시큐리티 필터임/api/login 주소 에서만 동작

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    private final CustomResponseUtil customResponseUtil;




//    @Autowired
//    UserRepository userRepository;
// bean스캔불가능....


    //생성자
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomResponseUtil customResponseUtil) {
        super(authenticationManager);
        this.customResponseUtil = customResponseUtil;
        setFilterProcessesUrl("/auth/login"); //주소 변경 기본은  /login
        this.authenticationManager = authenticationManager;
    }

    // Post : /api/login 이러면 동작
    //login시 이 필터를 제일 먼저 거친다
    // 여기서는 로그인 데이터를 받아 강제 로그인(세션) 진행 후
    //successfulAuthentication 메서드에서 jwt 발급받아서 전달해줌
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();

            //이건 한 번만 쓸 수 있음 !! 한 번 꺼낼 때 필요한 정보 저장해두기
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);
            String username = loginReqDto.getCode();

            String password = "";

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, password);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출함
            System.out.println("error");
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        customResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출됨");

        LoginUser loginUser = (LoginUser) authResult.getPrincipal(); //로그인 유저 정보
        String accessToken = JwtProcess.createAccessToken(loginUser); // 이 로그인 유저로 jwt 액세스 토큰 만들기

        response.addHeader(JwtVO.HEADER, accessToken); // 헤더에 토큰 담아




        customResponseUtil.success(response);
    }




}