package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AccountDetailResponse {
    private String name;
    private Long balance;
    private Long happyTotal;
    private Long recoverTotal;
    private LocalDateTime createdAt;
    private List<TransactionResponse> transactions;
}