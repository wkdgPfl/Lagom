package com.lagom.dto.request;

import com.lagom.global.EmotionType;
import com.lagom.global.ExpenseCategory;
import com.lagom.global.ExpenseType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ExpenseUpdateRequest {

    private ExpenseType type;

    private ExpenseCategory category;

    private String title;

    private Long amount;

    private String memo;

    private LocalDateTime paymentAt;

    private EmotionType emotion;

    private Integer evaluation;

    private List<String> repeatDays;

    private LocalDate repeatStartDate;
}