package com.lagom.repository;

import com.lagom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 카카오 ID로 유저 조회 (없으면 Optional.empty() 반환)
    Optional<User> findByKakaoId(String kakaoId);
}