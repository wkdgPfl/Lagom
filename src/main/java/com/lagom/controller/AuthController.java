package com.lagom.controller;

import com.lagom.dto.response.LoginResponse;
import com.lagom.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 카카오가 프론트로 전달한 인가코드를 받아서 로그인 처리
    // GET /auth/kakao?code=인가코드
    @GetMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
        return ResponseEntity.ok(authService.kakaoLogin(code));
    }
}