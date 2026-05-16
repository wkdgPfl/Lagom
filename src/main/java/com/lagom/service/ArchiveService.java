package com.lagom.service;

import com.lagom.domain.Account;
import com.lagom.domain.Transaction;
import com.lagom.dto.response.ArchiveDetailResponse;
import com.lagom.dto.response.ArchiveResponse;
import com.lagom.dto.response.TransactionResponse;
import com.lagom.exception.CustomException;
import com.lagom.repository.AccountRepository;
import com.lagom.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // 완료된 통장 목록 조회
    public List<ArchiveResponse> getArchives(Long userId) {
        return accountRepository.findByUserUserIdAndIsCompletedTrue(userId)
                .stream()
                .map(ArchiveResponse::new)
                .collect(Collectors.toList());
    }

    // 완료된 통장 상세 조회 (저금 내역 포함)
    public ArchiveDetailResponse getArchiveDetail(Long userId, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "통장을 찾을 수 없습니다"));

        // 본인 통장인지 확인
        if (!account.getUser().getUserId().equals(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        // 완료된 통장인지 확인
        if (!account.getIsCompleted()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "완료된 통장이 아닙니다");
        }

        List<TransactionResponse> transactions = transactionRepository
                .findByAccountAccountId(accountId)
                .stream()
                .map(TransactionResponse::from)
                .collect(Collectors.toList());

        return new ArchiveDetailResponse(account, transactions);
    }
}