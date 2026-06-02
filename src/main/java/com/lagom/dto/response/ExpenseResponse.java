package com.lagom.dto.response;

import com.lagom.domain.Expense;
import com.lagom.global.EmotionType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    private Long expenseId;
    private String title;
    private String type;
    private Long amount;
    private String category;
    private String memo;
    private String emotion;
    private Integer evaluation;

    private Boolean recommendSaving;
    private Boolean needReevaluation;

    private LocalDateTime paymentAt;
    private Boolean isRecurring;

    private String repeatCycle;
    private List<String> repeatDays;

    private LocalDate repeatStartDate;
    private LocalDate repeatEndDate;

    public static ExpenseResponse from(Expense expense) {

        if (expense == null) {
            return null;
        }

        EmotionType emotion = expense.getEmotion();
        Integer evaluation = expense.getEvaluation();

        boolean negativeEmotion =
                emotion == EmotionType.DEPRESSED
                        || emotion == EmotionType.STRESSED
                        || emotion == EmotionType.IMPULSIVE;

        boolean lowEvaluation =
                evaluation != null && evaluation <= 1;

        boolean highEvaluation =
                evaluation != null && evaluation >= 3;

        boolean recommendSaving =
                negativeEmotion && lowEvaluation;

        boolean needReevaluation =
                negativeEmotion
                        && highEvaluation
                        && Boolean.FALSE.equals(expense.getIsReevaluated())
                        && expense.getReevaluationAt() != null
                        && LocalDateTime.now().isAfter(expense.getReevaluationAt());

        List<String> repeatDaysList =
                (expense.getRepeatDays() == null || expense.getRepeatDays().isBlank())
                        ? List.of()
                        : Arrays.stream(expense.getRepeatDays().split(","))
                        .filter(s -> !s.isBlank())
                        .toList();

        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .title(expense.getTitle())

                .type(expense.getType() == null ? null : expense.getType().name())
                .amount(expense.getAmount())

                .category(expense.getCategory() == null ? null : expense.getCategory().name())
                .memo(expense.getMemo())

                .emotion(emotion == null ? null : emotion.name())
                .evaluation(evaluation)

                .recommendSaving(recommendSaving)
                .needReevaluation(needReevaluation)

                .paymentAt(expense.getPaymentAt())
                .isRecurring(expense.getIsRecurring())

                .repeatCycle(
                        expense.getRepeatCycle() == null
                                ? null
                                : expense.getRepeatCycle().name()
                )

                .repeatDays(repeatDaysList)

                .repeatStartDate(expense.getRepeatStartDate())
                .repeatEndDate(expense.getRepeatEndDate())

                .build();
    }
}