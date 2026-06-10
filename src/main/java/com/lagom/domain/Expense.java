package com.lagom.domain;

import com.lagom.global.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expense")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long amount;

    @Column(length = 300)
    private String memo;

    @Column(nullable = false)
    private LocalDateTime paymentAt;

    @Enumerated(EnumType.STRING)
    private EmotionType emotion;

    private Integer evaluation;

    @Column(nullable = false)
    private Boolean isReevaluated = false;

    @Column(nullable = false)
    private Boolean isRecurring;

    @Enumerated(EnumType.STRING)
    private RepeatCycle repeatCycle;

    @Column(length = 100)
    private String repeatDays = "";

    private LocalDate repeatStartDate;

    private LocalDate repeatEndDate;

    private LocalDate lastGeneratedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime reevaluationAt;

    @PrePersist
    public void onCreate() {

        this.createdAt = LocalDateTime.now();

        if (this.isReevaluated == null) {
            this.isReevaluated = false;
        }

        if (this.isRecurring == null) {
            this.isRecurring = false;
        }
    }

    public void update(
            String title,
            Long amount,
            ExpenseCategory category,
            String memo,
            LocalDateTime paymentAt,
            EmotionType emotion,
            Integer evaluation,
            Boolean isRecurring,
            RepeatCycle repeatCycle,
            List<String> repeatDays,
            LocalDate repeatStartDate,
            LocalDate repeatEndDate
    ) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.memo = memo;
        this.paymentAt = paymentAt;
        this.emotion = emotion;
        this.evaluation = evaluation;
        this.isRecurring =
                isRecurring != null
                        ? isRecurring
                        : false;
        this.repeatCycle = repeatCycle;
        this.repeatDays =
                repeatDays != null
                        ? String.join(",", repeatDays)
                        : null;

        this.repeatStartDate = repeatStartDate;
        this.repeatEndDate = repeatEndDate;

        if (emotion == null || evaluation == null) {
            this.reevaluationAt = null;
        } else {

            boolean negativeEmotion =
                    emotion == EmotionType.DEPRESSED
                            || emotion == EmotionType.STRESSED
                            || emotion == EmotionType.IMPULSIVE;

            boolean highEvaluation =
                    evaluation >= 3;

            this.reevaluationAt =
                    (negativeEmotion && highEvaluation)
                            ? paymentAt.plusDays(3)
                            : null;
        }
    }

    public void reevaluate(Integer evaluation) {

        this.evaluation = evaluation;
        this.isReevaluated = true;
    }

    public boolean shouldGenerateToday(LocalDate today) {

        if (!Boolean.TRUE.equals(isRecurring)) {
            return false;
        }

        if (repeatStartDate != null
                && today.isBefore(repeatStartDate)) {
            return false;
        }

        if (repeatEndDate != null
                && today.isAfter(repeatEndDate)) {
            return false;
        }

        if (lastGeneratedAt != null
                && lastGeneratedAt.equals(today)) {
            return false;
        }

        if (repeatCycle == RepeatCycle.DAILY) {
            return true;
        }

        if (repeatCycle == RepeatCycle.WEEKLY) {

            if (repeatDays == null) {
                return false;
            }

            String todayDay =
                    today.getDayOfWeek().name();

            List<String> days =
                    List.of(repeatDays.split(","));

            return days.contains(todayDay);
        }

        if (repeatCycle == RepeatCycle.MONTHLY) {

            if (repeatDays == null) {
                return false;
            }

            String day =
                    String.valueOf(today.getDayOfMonth());

            List<String> days =
                    List.of(repeatDays.split(","));

            return days.contains(day);
        }

        return false;
    }

    public void updateLastGeneratedAt(LocalDate date) {
        this.lastGeneratedAt = date;
    }
}