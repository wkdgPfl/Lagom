package com.lagom.controller;

import com.lagom.dto.response.MonthlyReportResponse;
import com.lagom.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 월별 리포트 조회
    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @AuthenticationPrincipal Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlyReport(userId, year, month));
    }

    // 데이터가 존재하는 연월 목록 반환
    @GetMapping("/available-months")
    public ResponseEntity<List<String>> getAvailableMonths(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(reportService.getAvailableMonths(userId));
    }
}