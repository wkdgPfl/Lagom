package com.lagom.controller;

import com.lagom.dto.request.NicknameUpdateRequest;
import com.lagom.dto.response.UserResponse;
import com.lagom.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getMe(userId));
    }

    // 닉네임 수정
    // JWT에서 꺼낸 userId를 @AuthenticationPrincipal로 주입받음
    @PatchMapping("/me/nickname")
    public ResponseEntity<UserResponse> updateNickname(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody NicknameUpdateRequest request) {
        return ResponseEntity.ok(userService.updateNickname(userId, request));
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // 204 반환
    }
}