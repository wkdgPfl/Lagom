package com.lagom.dto.response;

import com.lagom.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Getter
@Builder
public class TransactionResponse {

    private String type;
    private Long amount;
    private String memo;
    private List<String> tags;

    private String date;   // 날짜
    private String time;   // 시간
    private String nickname; // 사용자 닉네임

    private String createdAt;

    public static TransactionResponse from(Transaction t) {

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy", Locale.ENGLISH);

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return TransactionResponse.builder()
                .date(t.getCreatedAt().format(dateFormatter))
                .type(t.getType().name())
                .nickname(t.getAccount().getUser().getNickname())
                .time(t.getCreatedAt().format(timeFormatter))
                .memo(t.getMemo())
                .tags(
                        t.getTags() != null
                                ? List.of(t.getTags().split(","))
                                : List.of()
                )
                .amount(t.getAmount())
                .createdAt(t.getCreatedAt().toString())
                .build();
    }
}