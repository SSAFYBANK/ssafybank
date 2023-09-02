package com.ssafy.ssafybank.global.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.ssafy.ssafybank.domain.member.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;


@Component
public class CustomResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    //여기서 토큰 담아보자
    public void success(HttpServletResponse response) {
        try {
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("서버 파싱 에러");

        }
    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장하는 공통적인 응답 DTO를 만들어보자!!
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            log.error("서버 파싱 에러");
        }
    }
}