package com.lagom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lagom.global.GoalType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalType goalType;

    private Long goalAmount;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) this.balance = 0L;
        if (this.isCompleted == null) this.isCompleted = false;
    }

    public void deposit(Long amount) {
        this.balance += amount;
    }

    public void update(
            String name,
            GoalType goalType,
            Long goalAmount,
            LocalDate endDate
    ) {

        this.name = name;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.endDate = endDate;
    }

    public void complete() {
        this.isCompleted = true;
    }
}