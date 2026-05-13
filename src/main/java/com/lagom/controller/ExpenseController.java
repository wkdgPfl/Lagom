package com.lagom.controller;

import com.lagom.dto.request.ExpenseCreateRequest;
import com.lagom.dto.response.ExpenseResponse;
import com.lagom.dto.response.MonthlyCalendarResponse;
import com.lagom.dto.response.ReevaluateResponse;
import com.lagom.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse create(
            @RequestBody ExpenseCreateRequest request
    ) {
        return expenseService.create(request);
    }

    @GetMapping("/{id}")
    public ExpenseResponse detail(
            @PathVariable Long id
    ) {
        return expenseService.detail(id);
    }

    @GetMapping("/calendar/monthly")
    public MonthlyCalendarResponse monthly(
            @RequestParam Long userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return expenseService.getMonthlyCalendar(
                userId,
                year,
                month
        );
    }

    @GetMapping("/weekly")
    public List<ExpenseResponse> weekly(
            @RequestParam Long userId,
            @RequestParam String date
    ) {
        return expenseService.weekly(userId, date);
    }

    @PutMapping("/{id}")
    public ExpenseResponse update(
            @PathVariable Long id,
            @RequestBody ExpenseCreateRequest request
    ) {
        return expenseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        expenseService.delete(id);
    }

    @PatchMapping("/{id}/reevaluate")
    public ExpenseResponse reevaluate(
            @PathVariable("id") Long id,
            @RequestParam Integer evaluation
    ) {
        return expenseService.reevaluate(id, evaluation);
    }

    @GetMapping("/daily")
    public List<ExpenseResponse> daily(
            @RequestParam Long userId,
            @RequestParam LocalDate date
    ) {
        return expenseService.getDaily(userId, date);
    }

    @GetMapping("/reevaluation")
    public ReevaluateResponse getReevaluation(@RequestParam Long userId) {
        return expenseService.getReevaluationList(userId);
    }
}