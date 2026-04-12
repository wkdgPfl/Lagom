package com.lagom.dto.response;

import com.lagom.domain.User;
import lombok.Getter;

// 유저 정보를 프론트에 반환할 때 사용하는 DTO
// User 엔티티를 직접 반환하지 않고 DTO로 변환해서 반환 (보안상 이유)
@Getter
public class UserResponse {

    private Long userId;
    private String nickname;

    // User 엔티티를 받아서 DTO로 변환하는 생성자
    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
    }
}