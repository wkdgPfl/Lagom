package com.lagom.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 카카오 고유 ID로 유저 식별 (이메일 대신)
    @Column(nullable = false, unique = true)
    private String kakaoId;

    // 카카오에서 받은 닉네임, 수정 가능
    @Column(nullable = false)
    private String nickname;

    // 생성 후 수정 불가
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 유저 삭제 시 연관 데이터 함께 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 닉네임 수정 메서드 (setter 대신 의미있는 메서드명 사용)
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}