package com.ssafy.ssafybank.domain.member.controller;


import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class MemberController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping(value = "/oauth")
    public void kakaoConnect(HttpServletResponse response) throws IOException {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "0d71d6cba5e7587e9e8f923fe4fa9212");
        url.append("&redirect_uri=http://localhost:8080/login");
        url.append("&response_type=code");
        response.sendRedirect(String.valueOf(url));
    }

    @GetMapping(path = "/kakaoCallback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, HttpSession session , HttpServletResponse response) {
        System.out.println("cod" + code);
        String responses = "";
        String token = kakaoLoginService.getKakaoAccessToken(code);

        String id = kakaoLoginService.createKakaoUser(token);
        responses = id;
        System.out.println(id);

        return new ResponseEntity<>(id,  HttpStatus.OK);

    }
}
