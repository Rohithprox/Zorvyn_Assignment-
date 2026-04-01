package com.finance.backend.repository;

import com.finance.backend.entity.FinanceRecord;
import com.finance.backend.enums.RecordType;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class FinanceRecordSpecification {
    private FinanceRecordSpecification() {
    }

    public static Specification<FinanceRecord> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<FinanceRecord> hasType(RecordType type) {
        return (root, query, cb) -> type == null ? null : cb.equal(root.get("type"), type);
    }

    public static Specification<FinanceRecord> hasCategory(String category) {
        return (root, query, cb) ->
            (category == null || category.isBlank()) ? null : cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }

    public static Specification<FinanceRecord> dateFrom(LocalDate from) {
        return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<FinanceRecord> dateTo(LocalDate to) {
        return (root, query, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<FinanceRecord> buildFilter(RecordType type, String category, LocalDate from, LocalDate to) {
        return Specification.where(notDeleted()).and(hasType(type)).and(hasCategory(category)).and(dateFrom(from)).and(dateTo(to));
    }
}
