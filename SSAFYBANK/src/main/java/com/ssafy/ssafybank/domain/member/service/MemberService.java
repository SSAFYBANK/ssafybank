package com.ssafy.ssafybank.domain.member.service;

import com.ssafy.ssafybank.domain.member.entity.Member;
import com.ssafy.ssafybank.global.config.auth.LoginUser;

public interface MemberService {
     Member join(String kakaoId);

    String reissue(LoginUser loginUser);
}
