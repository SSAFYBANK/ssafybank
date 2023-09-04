package com.ssafy.ssafybank.domain.member.controller;


import com.ssafy.ssafybank.domain.accountHolder.dto.request.AccountHolderDelete;
import com.ssafy.ssafybank.domain.exchangeRate.dto.request.ExchangeRateRequestDto;
import com.ssafy.ssafybank.domain.exchangeRate.dto.response.ExchangeRateResponseDto;
import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.domain.member.repository.MemberRepository;
import com.ssafy.ssafybank.domain.member.service.KakaoLoginService;
import com.ssafy.ssafybank.domain.member.service.MemberService;
import com.ssafy.ssafybank.global.config.auth.LoginUser;
import com.ssafy.ssafybank.global.ex.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/member")
@RestController
public class MemberController {

    private final KakaoLoginService kakaoLoginService;
    private final MemberService memberService;
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

    @PostMapping("/reissue")
    public ResponseEntity<?> deleteAccountHolder(@AuthenticationPrincipal LoginUser loginUser) {
        String memberUuid = loginUser.getMember().getMemberUuid();
        String JWT = memberService.reissue(loginUser);
        return new ResponseEntity<>(new ResponseDto<>(1, "성공", JWT), HttpStatus.OK);
    }
}
