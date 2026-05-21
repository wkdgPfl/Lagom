package com.lagom.controller;

import com.lagom.domain.Transaction;
import com.lagom.dto.request.DepositRequest;
import com.lagom.dto.response.TransactionResponse;
import com.lagom.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public void deposit(@RequestBody DepositRequest request) {
        transactionService.deposit(request);
    }

    @GetMapping("/withdraw")
    public TransactionResponse withdraw(@RequestParam Long accountId) {
        return transactionService.withdraw(accountId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }
}