package com.lagom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 로그인 성공 시 프론트에 반환하는 응답 DTO
@Getter
@AllArgsConstructor
public class LoginResponse {
    // 발급된 JWT 토큰
    private String accessToken;
}