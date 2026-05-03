package com.lagom.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lagom.domain.Account;
import com.lagom.global.GoalType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class AccountResponse {

    private Long accountId;
    private String name;
    private Long balance;
    private GoalType goalType;

    private Long goalAmount;
    private LocalDate endDate;

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .name(account.getName())
                .balance(account.getBalance())
                .goalType(account.getGoalType())

                .goalAmount(
                        account.getGoalType() == GoalType.AMOUNT
                                ? account.getGoalAmount()
                                : null
                )
                .endDate(
                        account.getGoalType() == GoalType.PERIOD
                                ? account.getEndDate()
                                : null
                )

                .build();
    }
}