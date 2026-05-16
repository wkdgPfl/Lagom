package com.lagom.dto.response;

import com.lagom.domain.Account;
import com.lagom.global.GoalType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArchiveDetailResponse {

    private Long accountId;
    private String name;
    private Long balance;
    private GoalType goalType;
    private Long goalAmount;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private List<TransactionResponse> transactions;

    public ArchiveDetailResponse(Account account, List<TransactionResponse> transactions) {
        this.accountId = account.getAccountId();
        this.name = account.getName();
        this.balance = account.getBalance();
        this.goalType = account.getGoalType();
        this.goalAmount = account.getGoalType() == GoalType.AMOUNT ? account.getGoalAmount() : null;
        this.endDate = account.getGoalType() == GoalType.PERIOD ? account.getEndDate() : null;
        this.createdAt = account.getCreatedAt();
        this.transactions = transactions;
    }
}