package com.lagom.service;

import com.lagom.domain.Expense;
import com.lagom.domain.User;
import com.lagom.dto.request.ExpenseCreateRequest;
import com.lagom.dto.response.*;
import com.lagom.global.EmotionType;
import com.lagom.global.ExpenseType;
import com.lagom.repository.ExpenseRepository;
import com.lagom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseResponse create(ExpenseCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow();

        EmotionType emotion =
                request.getType() == ExpenseType.INCOME
                        ? null
                        : request.getEmotion();

        Integer evaluation =
                request.getType() == ExpenseType.INCOME
                        ? null
                        : request.getEvaluation();

        if (Boolean.TRUE.equals(request.getIsRecurring())
                && request.getRepeatCycle() == null) {

            throw new RuntimeException("반복 주기를 선택해 주세요.");
        }

        Expense expense = Expense.builder()
                .user(user)
                .type(request.getType())
                .category(request.getCategory())
                .title(request.getTitle())
                .amount(request.getAmount())
                .memo(request.getMemo())
                .paymentAt(request.getPaymentAt())

                .emotion(emotion)
                .evaluation(evaluation)

                .isRecurring(
                        request.getIsRecurring() != null
                                ? request.getIsRecurring()
                                : false
                )

                .repeatCycle(request.getRepeatCycle())

                .repeatDays(
                        request.getRepeatDays() != null
                                ? String.join(",", request.getRepeatDays())
                                : null
                )

                .repeatStartDate(request.getRepeatStartDate())
                .repeatEndDate(request.getRepeatEndDate())

                .reevaluationAt(
                        needReevaluation(
                                emotion,
                                evaluation
                        )
                                ? LocalDateTime.now().plusDays(3)
                                : null
                )

                .build();

        return ExpenseResponse.from(
                expenseRepository.save(expense)
        );
    }

    @Transactional(readOnly = true)
    public ExpenseResponse detail(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));

        return ExpenseResponse.from(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> monthly(
            Long userId,
            int year,
            int month
    ) {

        LocalDateTime start =
                LocalDate.of(year, month, 1)
                        .atStartOfDay();

        LocalDateTime end =
                start.plusMonths(1);

        return expenseRepository
                .findByUserUserIdAndPaymentAtBetween(
                        userId,
                        start,
                        end
                )
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> weekly(
            Long userId,
            String date
    ) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDate monday =
                localDate.minusDays(
                        localDate.getDayOfWeek().getValue() - 1
                );

        LocalDate sunday = monday.plusDays(6);

        return expenseRepository
                .findByUserUserIdAndPaymentAtBetween(
                        userId,
                        monday.atStartOfDay(),
                        sunday.atTime(23, 59, 59)
                )
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    public ExpenseResponse update(
            Long id,
            ExpenseCreateRequest request
    ) {

        EmotionType emotion =
                request.getType() == ExpenseType.INCOME
                        ? null
                        : request.getEmotion();

        Integer evaluation =
                request.getType() == ExpenseType.INCOME
                        ? null
                        : request.getEvaluation();

        if (Boolean.TRUE.equals(request.getIsRecurring())
                && request.getRepeatCycle() == null) {

            throw new RuntimeException("반복 주기를 선택해 주세요.");
        }

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found: " + id));

        expense.update(
                request.getTitle(),
                request.getAmount(),
                request.getCategory(),
                request.getMemo(),
                request.getPaymentAt(),

                emotion,
                evaluation,

                request.getIsRecurring(),
                request.getRepeatCycle(),
                request.getRepeatDays(),
                request.getRepeatStartDate(),
                request.getRepeatEndDate()
        );

        return ExpenseResponse.from(expense);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }

    private boolean needReevaluation(
            EmotionType emotion,
            Integer evaluation
    ) {

        if (emotion == null || evaluation == null) {
            return false;
        }

        boolean negativeEmotion =
                emotion == EmotionType.DEPRESSED
                        || emotion == EmotionType.STRESSED
                        || emotion == EmotionType.IMPULSIVE;

        boolean highEvaluation =
                evaluation >= 3;

        return negativeEmotion && highEvaluation;
    }

    public ExpenseResponse reevaluate(
            Long id,
            Integer evaluation
    ) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow();

        expense.reevaluate(evaluation);

        return ExpenseResponse.from(expense);
    }

    public MonthlyCalendarResponse getMonthlyCalendar(
            Long userId,
            int year,
            int month
    ) {

        List<Expense> list =
                expenseRepository.findByUserUserId(userId);

        list = list.stream()
                .filter(e ->
                        e.getPaymentAt().getYear() == year
                                && e.getPaymentAt().getMonthValue() == month
                )
                .toList();

        Long income = list.stream()
                .filter(e -> e.getType() == ExpenseType.INCOME)
                .mapToLong(Expense::getAmount)
                .sum();

        Long expense = list.stream()
                .filter(e -> e.getType() == ExpenseType.EXPENSE)
                .mapToLong(Expense::getAmount)
                .sum();

        Long reevaluationCount = list.stream()
                .filter(e ->
                        Boolean.FALSE.equals(e.getIsReevaluated())
                                && e.getReevaluationAt() != null
                                && LocalDateTime.now().isAfter(e.getReevaluationAt())
                )
                .count();

        Map<LocalDate, List<Expense>> grouped =
                list.stream()
                        .collect(Collectors.groupingBy(
                                e -> e.getPaymentAt().toLocalDate()
                        ));

        List<CalendarDayResponse> days =
                grouped.entrySet().stream()
                        .map(entry -> {

                            Long dayIncome =
                                    entry.getValue().stream()
                                            .filter(e -> e.getType() == ExpenseType.INCOME)
                                            .mapToLong(Expense::getAmount)
                                            .sum();

                            Long dayExpense =
                                    entry.getValue().stream()
                                            .filter(e -> e.getType() == ExpenseType.EXPENSE)
                                            .mapToLong(Expense::getAmount)
                                            .sum();

                            return CalendarDayResponse.builder()
                                    .date(entry.getKey().toString())
                                    .income(dayIncome)
                                    .expense(dayExpense)
                                    .build();
                        })
                        .toList();

        return MonthlyCalendarResponse.builder()
                .monthIncome(income)
                .monthExpense(expense)
                .reevaluationCount(reevaluationCount)
                .days(days)
                .build();
    }

    public List<ExpenseResponse> getDaily(
            Long userId,
            LocalDate date
    ) {

        return expenseRepository.findByUserUserId(userId)
                .stream()
                .filter(e ->
                        e.getPaymentAt()
                                .toLocalDate()
                                .equals(date)
                )
                .map(ExpenseResponse::from)
                .toList();
    }

    public ReevaluateResponse getReevaluationList(Long userId) {

        List<Expense> list = expenseRepository.findByUserUserId(userId);

        List<Expense> target = expenseRepository.findReevaluationTarget(userId);

        return ReevaluateResponse.builder()
                .count((long) target.size())
                .items(
                        target.stream()
                                .map(e -> ReevaluateItemResponse.builder()
                                        .expenseId(e.getExpenseId())
                                        .type(e.getType().name())
                                        .title(e.getTitle())
                                        .category(e.getCategory().name())
                                        .amount(e.getAmount())
                                        .emotion(e.getEmotion().name())
                                        .evaluation(e.getEvaluation())
                                        .paymentAt(e.getPaymentAt())
                                        .reevaluationAt(e.getReevaluationAt())
                                        .build()
                                )
                                .toList()
                )
                .build();
    }
}