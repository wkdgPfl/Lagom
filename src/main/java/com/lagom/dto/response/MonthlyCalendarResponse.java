package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MonthlyCalendarResponse {

    private Long monthIncome;

    private Long monthExpense;

    private Long reevaluationCount;

    private List<CalendarDayResponse> days;
}