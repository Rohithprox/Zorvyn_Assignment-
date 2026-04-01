package com.finance.backend.repository;

import com.finance.backend.dto.response.CategorySummaryResponse;
import com.finance.backend.dto.response.MonthlyTrendResponse;
import com.finance.backend.entity.FinanceRecord;
import com.finance.backend.enums.RecordType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long>, JpaSpecificationExecutor<FinanceRecord> {

    Optional<FinanceRecord> findByIdAndIsDeletedFalse(Long id);
    Page<FinanceRecord> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinanceRecord r WHERE r.isDeleted = false AND r.type = :type")
    BigDecimal sumByType(@Param("type") RecordType type);

    @Query("SELECT COUNT(r) FROM FinanceRecord r WHERE r.isDeleted = false")
    Long countActive();

    @Query("SELECT new com.finance.backend.dto.response.CategorySummaryResponse(r.category, SUM(r.amount), COUNT(r)) " +
        "FROM FinanceRecord r WHERE r.isDeleted = false GROUP BY r.category ORDER BY SUM(r.amount) DESC")
    List<CategorySummaryResponse> getCategoryTotals();

    @Query("SELECT new com.finance.backend.dto.response.MonthlyTrendResponse(" +
        "CONCAT(YEAR(r.date), '-', LPAD(CAST(MONTH(r.date) AS string), 2, '0')), " +
        "SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END), " +
        "SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END), " +
        "SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE -r.amount END)) " +
        "FROM FinanceRecord r WHERE r.isDeleted = false AND r.date >= :since " +
        "GROUP BY YEAR(r.date), MONTH(r.date) ORDER BY YEAR(r.date), MONTH(r.date)")
    List<MonthlyTrendResponse> getMonthlyTrend(@Param("since") LocalDate since);

    @Query("SELECT r FROM FinanceRecord r WHERE r.isDeleted = false ORDER BY r.date DESC, r.createdAt DESC")
    List<FinanceRecord> findRecentRecords(Pageable pageable);
}
