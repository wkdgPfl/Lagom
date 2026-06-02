package com.lagom.scheduler;

import com.lagom.domain.Expense;
import com.lagom.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseScheduler {

    private final ExpenseRepository expenseRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void generateRecurringExpenses() {

        LocalDate today = LocalDate.now();

        List<Expense> recurringList =
                expenseRepository.findByIsRecurringTrue();

        for (Expense expense : recurringList) {

            if (!expense.shouldGenerateToday(today)) {
                continue;
            }

            Expense newExpense = Expense.builder()
                    .user(expense.getUser())
                    .type(expense.getType())
                    .category(expense.getCategory())
                    .title(expense.getTitle())
                    .amount(expense.getAmount())
                    .memo(expense.getMemo())

                    .paymentAt(
                            today.atStartOfDay()
                    )

                    .emotion(null)
                    .evaluation(null)

                    .isRecurring(false)

                    .build();

            expenseRepository.save(newExpense);

            expense.updateLastGeneratedAt(today);
        }
    }
}