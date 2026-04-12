package com.lagom.service;

import com.lagom.domain.User;
import com.lagom.dto.request.NicknameUpdateRequest;
import com.lagom.dto.response.UserResponse;
import com.lagom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final KakaoOAuthService kakaoOAuthService;

    // 닉네임 수정
    @Transactional
    public UserResponse updateNickname(Long userId, NicknameUpdateRequest request) {
        // userId로 유저 조회, 없으면 예외
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 닉네임 수정
        user.updateNickname(request.getNickname());

        // 수정된 유저 정보 반환
        return new UserResponse(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        // 카카오 연결 끊기 (동의항목 초기화)
        kakaoOAuthService.unlink(user.getKakaoId());

        // DB에서 유저 삭제
        userRepository.delete(user);
    }
}