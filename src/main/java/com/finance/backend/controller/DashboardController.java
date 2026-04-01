package com.finance.backend.controller;

import com.finance.backend.dto.response.CategorySummaryResponse;
import com.finance.backend.dto.response.DashboardSummaryResponse;
import com.finance.backend.dto.response.FinanceRecordResponse;
import com.finance.backend.dto.response.MonthlyTrendResponse;
import com.finance.backend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Dashboard", description = "Summary and analytics endpoints")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Total income, expenses, net balance, and record count")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/by-category")
    @Operation(summary = "Total amount and count grouped by category")
    public ResponseEntity<List<CategorySummaryResponse>> getCategoryBreakdown() {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown());
    }

    @GetMapping("/monthly-trend")
    @Operation(summary = "Income vs expenses for each month in the last 12 months")
    public ResponseEntity<List<MonthlyTrendResponse>> getMonthlyTrend() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrend());
    }

    @GetMapping("/recent")
    @Operation(summary = "Last 10 financial records")
    public ResponseEntity<List<FinanceRecordResponse>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }
}
