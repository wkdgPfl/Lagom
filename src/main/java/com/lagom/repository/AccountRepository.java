package com.lagom.repository;

import com.lagom.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserId(Long userId);

    // 진행 중인 통장 목록 조회
    List<Account> findByUserUserIdAndIsCompletedFalse(Long userId);

    // 완료된 통장 목록 조회
    List<Account> findByUserUserIdAndIsCompletedTrue(Long userId);
}