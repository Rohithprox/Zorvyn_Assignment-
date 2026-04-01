package com.finance.backend.controller;

import com.finance.backend.dto.request.CreateRecordRequest;
import com.finance.backend.dto.request.UpdateRecordRequest;
import com.finance.backend.dto.response.FinanceRecordResponse;
import com.finance.backend.enums.RecordType;
import com.finance.backend.service.FinanceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Finance Records", description = "Create, view, update, and delete financial records")
public class FinanceRecordController {
    private final FinanceRecordService recordService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List records with optional filters and pagination",
        description = "Filter by type (INCOME/EXPENSE), category, date range. Supports pagination.")
    public ResponseEntity<Page<FinanceRecordResponse>> getRecords(
        // I know this could be a FilterRequest DTO but keeping it flat
        // since we only have 4 params and wrapping felt overkill.
        @RequestParam(required = false) RecordType type,
        @RequestParam(required = false) String category,
        @Parameter(description = "Start date (yyyy-MM-dd)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "End date (yyyy-MM-dd)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @PageableDefault(size = 10, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(recordService.getRecords(type, category, from, to, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a single record by ID")
    public ResponseEntity<FinanceRecordResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Create a new financial record")
    public ResponseEntity<FinanceRecordResponse> create(@Valid @RequestBody CreateRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Update a financial record (partial update - only provided fields change)")
    public ResponseEntity<FinanceRecordResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateRecordRequest request) {
        return ResponseEntity.ok(recordService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft-delete a record (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
