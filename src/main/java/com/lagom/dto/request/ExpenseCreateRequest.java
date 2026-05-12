package com.lagom.dto.request;

import com.lagom.global.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExpenseCreateRequest {

    private Long userId;

    private ExpenseType type;

    private ExpenseCategory category;

    private String title;

    private Long amount;

    private String memo;

    private LocalDateTime paymentAt;

    private EmotionType emotion;

    private Integer evaluation;

    private Boolean isRecurring;

    private RepeatCycle repeatCycle;

    private List<String> repeatDays;

    private LocalDate repeatStartDate;

    private LocalDate repeatEndDate;
}