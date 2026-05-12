package com.lagom.repository;

import com.lagom.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserUserId(Long userId);

    List<Expense> findByUserUserIdAndPaymentAtBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Expense> findByIsRecurringTrue();
}