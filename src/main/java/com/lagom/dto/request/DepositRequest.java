package com.lagom.dto.request;

import com.lagom.global.TransactionType;
import lombok.Getter;

import java.util.List;

@Getter
public class DepositRequest {
    private Long accountId;
    private TransactionType type;
    private Long amount;
    private String memo;
    private List<String> tags;
}