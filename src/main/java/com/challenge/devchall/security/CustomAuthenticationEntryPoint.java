package com.challenge.devchall.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 사용자에게 인증이 필요하다는 메시지를 응답으로 전송하거나 로그인 페이지로 리다이렉트 등의 작업을 수행합니다.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "가입되지 않은 사용자 접근");
        response.sendRedirect("usr/member/login");
    }
}
