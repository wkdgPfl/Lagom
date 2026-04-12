package com.lagom.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// 카카오 API가 반환하는 유저정보 JSON을 Java 객체로 매핑
@Getter
public class KakaoUserInfo {

    // 카카오 고유 식별자
    private Long id;

    // JSON 키가 스네이크케이스(kakao_account)라서 @JsonProperty로 매핑
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        private String email;
        private KakaoProfile profile;

        @Getter
        public static class KakaoProfile {
            private String nickname;
        }
    }

    // 편의 메서드: 이메일 바로 꺼내기
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    // 편의 메서드: 닉네임 바로 꺼내기
    public String getNickname() {
        return kakaoAccount.getProfile().getNickname();
    }
}