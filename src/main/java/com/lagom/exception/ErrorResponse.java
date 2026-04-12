package com.lagom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 에러 응답 형식 통일
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}