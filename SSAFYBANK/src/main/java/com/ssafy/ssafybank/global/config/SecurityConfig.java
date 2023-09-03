package com.ssafy.ssafybank.global.config;


import com.ssafy.ssafybank.global.config.jwt.JwtAuthenticationFilter;
import com.ssafy.ssafybank.global.config.jwt.JwtAuthorizationFilter;
import com.ssafy.ssafybank.global.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CustomResponseUtil customResponseUtil;

    @Autowired
    public SecurityConfig(CustomResponseUtil customResponseUtil) {
        this.customResponseUtil = customResponseUtil;
    }

    // JWT 필터 등록이 필요함 정해진 형식이 있는거라서 외워야 함
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception { //여기서 필터 등록
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);//강제 세션로그인 하기 위해서 필요
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, customResponseUtil)); //필터 등록
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }
    @Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨"); //이렇게 디버그로 잘 실행이 되는지 화깅ㄴ하자!
        return new BCryptPasswordEncoder();
    }

    // JWT 서버를 만들 예정!! Session 사용안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        http.headers().frameOptions().sameOrigin(); // iframe 허용안함.
        http.csrf().disable(); // enable이면 post맨 작동안함
        http.cors().configurationSource(configurationSource());

        // jSessionId를 서버쪽에서 관리안하겠다는 뜻!!
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // react, 앱으로 요청할 예정
        http.formLogin().disable();
        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();

        http.apply(new CustomSecurityFilterManager());

        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {


            CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
        });

        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
        });

        http.authorizeRequests()
                .antMatchers("/v1/**").authenticated() //포스트맨으로 http://localhost:8081/api/admin/dsd 이런식으로 테스트 해보면 403 에러 확인
                .antMatchers("/api/admin/**").hasRole("" + "ADMIN") // 최근 공식문서에서는 ROLE_ 안붙여도 됨

                .anyRequest().permitAll(); //나머지 요청은 허용

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react 나중에 바꺼야함)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);//모든 주소 요청에 위 설정을 적용하겠다.
        return source;
    }

}