package com.lagom.service;

import com.lagom.domain.Account;
import com.lagom.domain.Transaction;
import com.lagom.dto.request.DepositRequest;
import com.lagom.dto.response.TransactionResponse;
import com.lagom.global.TransactionType;
import com.lagom.repository.AccountRepository;
import com.lagom.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void deposit(DepositRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow();

        account.deposit(request.getAmount());

        Transaction tx = Transaction.builder()
                .account(account)
                .type(request.getType())
                .amount(request.getAmount())
                .memo(request.getMemo())
                .tags(
                        request.getTags() != null
                                ? String.join(",", request.getTags())
                                : null
                )
                .build();

        transactionRepository.save(tx);
    }

    public TransactionResponse withdraw(Long accountId) {

        List<Transaction> list =
                transactionRepository.findByAccountAccountId(accountId)
                        .stream()
                        .filter(t -> t.getType() == TransactionType.HAPPY)
                        .toList();

        if (list.isEmpty()) throw new RuntimeException("기록이 존재하지 않습니다.");

        Transaction t = list.get(new Random().nextInt(list.size()));

        return TransactionResponse.from(t);
    }
}