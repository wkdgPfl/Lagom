package com.lagom.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReevaluateResponse {
    private Long count;
    private List<ReevaluateItemResponse> items;
}
