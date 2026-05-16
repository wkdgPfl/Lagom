package com.lagom.service;

import com.lagom.domain.Expense;
import com.lagom.dto.response.MonthlyReportResponse;
import com.lagom.global.ExpenseType;
import com.lagom.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseRepository expenseRepository;

    public MonthlyReportResponse getMonthlyReport(Long userId, int year, int month) {
        // 해당 월의 시작/끝 날짜 계산
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        // 해당 월 지출 데이터 조회
        List<Expense> expenses = expenseRepository
                .findByUserUserIdAndPaymentAtBetween(userId, start, end);

        // 1. 이달 총 지출 (EXPENSE 타입만)
        Long totalExpense = expenses.stream()
                .filter(e -> e.getType() == ExpenseType.EXPENSE)
                .mapToLong(Expense::getAmount)
                .sum();

        // 2. 감정이 기록된 건수
        List<Expense> withEmotion = expenses.stream()
                .filter(e -> e.getEmotion() != null)
                .collect(Collectors.toList());

        int emotionCount = withEmotion.size();

        // 3. 감정별 소비 비율 (%)
        Map<String, Long> emotionExpenseSum = withEmotion.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getEmotion().name(),
                        Collectors.summingLong(Expense::getAmount)
                ));

        Long totalWithEmotion = emotionExpenseSum.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Double> emotionExpenseRatio = emotionExpenseSum.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> totalWithEmotion == 0 ? 0.0
                                : Math.round((e.getValue() * 100.0 / totalWithEmotion) * 10) / 10.0
                ));

        // 4. 감정별 평균 만족도
        Map<String, Double> emotionAvgEvaluation = withEmotion.stream()
                .filter(e -> e.getEvaluation() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getEmotion().name(),
                        Collectors.averagingDouble(e -> e.getEvaluation().doubleValue())
                ));

        // 소수점 1자리로 반올림
        emotionAvgEvaluation = emotionAvgEvaluation.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Math.round(e.getValue() * 10) / 10.0
                ));

        return MonthlyReportResponse.builder()
                .totalExpense(totalExpense)
                .emotionCount(emotionCount)
                .emotionExpenseRatio(emotionExpenseRatio)
                .emotionAvgEvaluation(emotionAvgEvaluation)
                .build();
    }
}