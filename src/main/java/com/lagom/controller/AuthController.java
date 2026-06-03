package com.lagom.controller;

import com.lagom.dto.response.LoginResponse;
import com.lagom.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 카카오가 프론트로 전달한 인가코드를 받아서 로그인 처리
    // GET /auth/kakao?code=인가코드
    // 카카오 인가코드로 로그인 처리 후 프론트로 JWT 리다이렉트
    @GetMapping("/kakao")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        LoginResponse loginResponse = authService.kakaoLogin(code);
        response.sendRedirect("https://happeach.site?token=" + loginResponse.getAccessToken());
    }
}