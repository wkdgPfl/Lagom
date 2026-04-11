package com.lagom.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;
    private final long expiration;

    // application.yml의 jwt.secret, jwt.expiration 값을 주입받음
    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        // 시크릿 키를 HMAC-SHA 알고리즘용 Key 객체로 변환
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // 로그인 성공 시 userId를 담은 JWT 생성
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))   // 토큰 주체 = userId
                .setIssuedAt(new Date())               // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    // 토큰에서 userId 추출 (API 요청마다 호출됨)
    public Long getUserId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject() // subject에 저장한 userId 꺼냄
        );
    }

    // 토큰 유효성 검증 (만료, 위조 여부 확인)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 만료되거나 위조된 토큰이면 false 반환
            return false;
        }
    }
}