package com.finance.backend.service;

import com.finance.backend.dto.response.CategorySummaryResponse;
import com.finance.backend.dto.response.DashboardSummaryResponse;
import com.finance.backend.dto.response.FinanceRecordResponse;
import com.finance.backend.dto.response.MonthlyTrendResponse;
import com.finance.backend.enums.RecordType;
import com.finance.backend.repository.FinanceRecordRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final FinanceRecordRepository recordRepository;
    private final FinanceRecordService recordService;

    public DashboardSummaryResponse getSummary() {
        BigDecimal income = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal expenses = recordRepository.sumByType(RecordType.EXPENSE);
        return DashboardSummaryResponse.builder()
            .totalIncome(income)
            .totalExpenses(expenses)
            .netBalance(income.subtract(expenses))
            .totalRecords(recordRepository.countActive())
            .build();
    }

    public List<CategorySummaryResponse> getCategoryBreakdown() {
        return recordRepository.getCategoryTotals();
    }

    public List<MonthlyTrendResponse> getMonthlyTrend() {
        LocalDate since = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        return recordRepository.getMonthlyTrend(since);
    }

    public List<FinanceRecordResponse> getRecentActivity() {
        return recordRepository.findRecentRecords(PageRequest.of(0, 10)).stream().map(recordService::toResponse).toList();
    }
}
