package com.lagom.repository;

import com.lagom.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("""
    SELECT e FROM Expense e
    WHERE e.user.userId = :userId
    AND e.isReevaluated = false
    AND e.reevaluationAt IS NOT NULL
    AND e.reevaluationAt <= CURRENT_TIMESTAMP
    """)
    List<Expense> findReevaluationTarget(Long userId);
}