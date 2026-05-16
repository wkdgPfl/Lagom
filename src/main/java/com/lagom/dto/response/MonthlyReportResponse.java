package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MonthlyReportResponse {

    // 이달 총 지출
    private Long totalExpense;

    // 기록된 감정 건수
    private int emotionCount;

    // 감정별 소비 비율 (감정 -> 퍼센트)
    private Map<String, Double> emotionExpenseRatio;

    // 감정별 평균 만족도 (감정 -> 평균 만족도)
    private Map<String, Double> emotionAvgEvaluation;
}