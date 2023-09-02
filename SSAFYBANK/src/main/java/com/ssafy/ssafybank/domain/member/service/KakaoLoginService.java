package com.ssafy.ssafybank.domain.member.service;

public interface KakaoLoginService {
    String getKakaoAccessToken (String code);

    String createKakaoUser(String token);
}
