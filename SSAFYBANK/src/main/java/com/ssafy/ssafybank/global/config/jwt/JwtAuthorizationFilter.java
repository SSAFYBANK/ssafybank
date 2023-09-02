package com.ssafy.ssafybank.global.config.jwt;


import com.ssafy.ssafybank.global.config.auth.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 모든 주소에서 동작함 (토큰 검증)
 */
//BasicAuthenticationFilter 시큐리티 필터임
//securityconfig에서 따로 등록이 필요함
//인가필터. login에서는 이 필터가 낚아채지 않고 login필터가 낚아챔
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // JWT 토큰 헤더를 추가하지 않아도 해당 필터는 통과는 할 수 있지만, 결국 시큐리티단에서 세션 값 검증에 실패함.
    // 헤더가 없으면 바로  chain.doFilter(request, response); 실행돼서
    //security타고 바로 컨트롤러진입 시도
    //해당 컨트롤러 주소가 api/s 이면 security 체인에 있는
    //http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
    //CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
    //이거 발동해서 exception 발생 시킨다.

    //api/s/* 이런식의 주소가 들어오면 메서드 실행
    //헤더에 JWT 있는지 확인 및 검증함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            // 토큰이 존재함
            log.debug("디버그 : 토큰이 존재함");

            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
            LoginUser loginUser = JwtProcess.verifyAccessToken(token);//검증
            log.debug("디버그 : 토큰이 검증이 완료됨");

            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                    loginUser.getAuthorities()); // id, role 만 존재 ,비밀번호는 null 왜냐하면 JwtProcess.verify(token)

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 임시 세션이 생성됨");
        }
        chain.doFilter(request, response); //security의 다음 필터로 이동해라
    }
    //헤더 검증 메서드
    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        //리프레시 토큰 헤더까지 추가
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX) || !header.startsWith("RefreshToken")) {
            return false;
        } else {
            return true;
        }
    }
}