package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReevaluateItemResponse {
    private Long expenseId;
    private String title;
    private Long amount;
    private String emotion;
    private Integer evaluation;
    private LocalDateTime reevaluationAt;
}
