package com.lagom.dto.response;

import com.lagom.domain.Account;
import com.lagom.global.GoalType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ArchiveResponse {

    private Long accountId;
    private String name;
    private Long balance;
    private GoalType goalType;
    private Long goalAmount;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public ArchiveResponse(Account account) {
        this.accountId = account.getAccountId();
        this.name = account.getName();
        this.balance = account.getBalance();
        this.goalType = account.getGoalType();
        this.goalAmount = account.getGoalType() == GoalType.AMOUNT ? account.getGoalAmount() : null;
        this.endDate = account.getGoalType() == GoalType.PERIOD ? account.getEndDate() : null;
        this.createdAt = account.getCreatedAt();
    }
}