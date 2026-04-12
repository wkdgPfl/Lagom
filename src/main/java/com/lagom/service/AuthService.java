package com.lagom.service;

import com.lagom.domain.User;
import com.lagom.dto.response.KakaoUserInfo;
import com.lagom.dto.response.LoginResponse;
import com.lagom.jwt.JwtProvider;
import com.lagom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final JwtProvider jwtProvider;

    public LoginResponse kakaoLogin(String code) {
        // 1. 인가코드로 카카오 액세스토큰 발급
        String kakaoAccessToken = kakaoOAuthService.getAccessToken(code);

        // 2. 액세스토큰으로 카카오 유저정보(id, 닉네임) 조회
        KakaoUserInfo kakaoUserInfo = kakaoOAuthService.getUserInfo(kakaoAccessToken);

        // 3. 카카오 ID를 String으로 변환 (DB 저장 타입이 String)
        String kakaoId = String.valueOf(kakaoUserInfo.getId());

        // 4. DB에서 카카오 ID로 유저 조회
        //    없으면 자동 회원가입, 있으면 그냥 로그인
        User user = userRepository.findByKakaoId(kakaoId)
                // 없으면 자동 회원가입
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .kakaoId(kakaoId)
                                .nickname(kakaoUserInfo.getNickname())
                                .build()
                ));

        // 5. userId로 JWT 발급해서 프론트에 반환
        String token = jwtProvider.generateToken(user.getUserId());
        return new LoginResponse(token);
    }
}