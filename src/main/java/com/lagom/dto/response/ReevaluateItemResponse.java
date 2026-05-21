package com.lagom.dto.response;

import com.lagom.domain.Expense;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReevaluateItemResponse {
    private Long expenseId;
    private String type;
    private String title;
    private String category;
    private Long amount;
    private String emotion;
    private Integer evaluation;
    private LocalDateTime paymentAt;
    private LocalDateTime reevaluationAt;

    public static ReevaluateItemResponse from(Expense e) {

        return ReevaluateItemResponse.builder()
                .expenseId(e.getExpenseId())
                .type(e.getType().name())
                .title(e.getTitle())
                .category(e.getCategory().name())
                .amount(e.getAmount())
                .emotion(e.getEmotion() == null ? null : e.getEmotion().name())
                .evaluation(e.getEvaluation())
                .paymentAt(e.getPaymentAt())
                .reevaluationAt(e.getReevaluationAt())
                .build();
    }
}