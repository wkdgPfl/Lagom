package com.lagom.service;

import com.lagom.domain.Account;
import com.lagom.domain.Transaction;
import com.lagom.domain.User;
import com.lagom.dto.response.AccountDetailResponse;
import com.lagom.dto.response.AccountResponse;
import com.lagom.dto.response.TransactionResponse;
import com.lagom.global.GoalType;
import com.lagom.global.TransactionType;
import com.lagom.repository.AccountRepository;
import com.lagom.repository.TransactionRepository;
import com.lagom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public Long createAccount(Long userId, String name, GoalType goalType, Long goalAmount, LocalDate endDate) {

        User user = userRepository.findById(userId).orElseThrow();

        Account account = Account.builder()
                .user(user)
                .name(name != null ? name : "행복 통장")
                .goalType(goalType)
                .goalAmount(goalAmount)
                .endDate(endDate)
                .balance(0L)
                .isCompleted(false)
                .build();

        return accountRepository.save(account).getAccountId();
    }

    public List<AccountResponse> getAccounts(Long userId) {
        return accountRepository.findByUserUserId(userId)
                .stream()
                .map(AccountResponse::from)
                .toList();
    }

    public AccountDetailResponse getAccountDetail(Long accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow();

        List<Transaction> list = transactionRepository.findByAccountAccountId(accountId);

        Long happy = list.stream()
                .filter(t -> t.getType() == TransactionType.HAPPY)
                .mapToLong(Transaction::getAmount).sum();

        Long recover = list.stream()
                .filter(t -> t.getType() == TransactionType.RECOVER)
                .mapToLong(Transaction::getAmount).sum();

        List<TransactionResponse> txList = list.stream()
                .map(TransactionResponse::from)
                .toList();

        return AccountDetailResponse.builder()
                .name(account.getName())
                .balance(account.getBalance())
                .happyTotal(happy)
                .recoverTotal(recover)
                .transactions(txList)
                .build();
    }

    public void deposit(Account account, Long amount) {
        account.deposit(amount);
    }
}
