package com.lagom.controller;

import com.lagom.domain.Account;
import com.lagom.dto.request.AccountUpdateRequest;
import com.lagom.dto.response.AccountDetailResponse;
import com.lagom.dto.response.AccountResponse;
import com.lagom.global.GoalType;
import com.lagom.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Long create(@RequestParam Long userId,
                       @RequestParam String name,
                       @RequestParam GoalType goalType,
                       @RequestParam(required = false) Long goalAmount,
                       @RequestParam(required = false) LocalDate endDate) {

        return accountService.createAccount(userId, name, goalType, goalAmount, endDate);
    }

    @GetMapping
    public List<AccountResponse> list(@RequestParam Long userId) {
        return accountService.getAccounts(userId);
    }

    @GetMapping("/{id}")
    public AccountDetailResponse detail(@PathVariable Long id) {
        return accountService.getAccountDetail(id);
    }

    @PutMapping("/{id}")
    public AccountResponse update(
            @PathVariable Long id,
            @RequestBody AccountUpdateRequest request
    ) {

        return accountService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {

        accountService.delete(id);
    }

    @PatchMapping("/{id}/complete")
    public AccountResponse complete(
            @PathVariable Long id
    ) {

        return accountService.complete(id);
    }
}