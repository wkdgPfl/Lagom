package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccountDetailResponse {
    private String name;
    private Long balance;
    private Long happyTotal;
    private Long recoverTotal;
    private List<TransactionResponse> transactions;
}