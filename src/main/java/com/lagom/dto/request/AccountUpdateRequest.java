package com.lagom.dto.request;

import com.lagom.global.GoalType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountUpdateRequest {

    private String name;

    private GoalType goalType;

    private Long goalAmount;

    private LocalDate endDate;
}