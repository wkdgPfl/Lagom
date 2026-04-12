package com.lagom.service;

import com.lagom.dto.response.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    // application.yml에서 카카오 설정값 주입
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.admin-key}")
    private String adminKey;

    // 프론트에서 받은 인가코드로 카카오 액세스토큰 발급
    public String getAccessToken(String code) {
        return WebClient.create()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                // 카카오 토큰 발급에 필요한 파라미터
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                // 에러 응답 본문을 로그로 출력
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .doOnNext(body -> System.out.println("=== 카카오 에러 응답 ===\n" + body))
                                .flatMap(body -> reactor.core.publisher.Mono.error(
                                        new RuntimeException("카카오 401: " + body)))
                )
                .bodyToMono(java.util.Map.class)
                .map(body -> (String) body.get("access_token")) // 응답에서 액세스토큰만 추출
                .block(); // 응답 올 때까지 동기 대기
    }

    // 카카오 액세스토큰으로 유저 이메일, 닉네임 조회
    public KakaoUserInfo getUserInfo(String accessToken) {
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfo.class) // JSON 응답을 KakaoUserInfo 객체로 변환
                .block();
    }

    // 카카오 연결 끊기 (회원 탈퇴 시 호출)
    // 카카오 연결 끊기 - Admin 키 사용
    public void unlink(String kakaoId) {
        WebClient.create()
                .post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .header("Authorization", "KakaoAK " + adminKey)  // clientId → adminKey
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("target_id_type", "user_id")
                        .with("target_id", kakaoId))
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .block();
    }
}