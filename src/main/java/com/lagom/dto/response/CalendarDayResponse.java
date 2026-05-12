package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarDayResponse {

    private String date;

    private Long income;

    private Long expense;
}