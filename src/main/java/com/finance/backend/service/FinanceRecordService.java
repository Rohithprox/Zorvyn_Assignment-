package com.finance.backend.service;

import com.finance.backend.dto.request.CreateRecordRequest;
import com.finance.backend.dto.request.UpdateRecordRequest;
import com.finance.backend.dto.response.FinanceRecordResponse;
import com.finance.backend.entity.FinanceRecord;
import com.finance.backend.entity.User;
import com.finance.backend.enums.RecordType;
import com.finance.backend.exception.ResourceNotFoundException;
import com.finance.backend.repository.FinanceRecordRepository;
import com.finance.backend.repository.FinanceRecordSpecification;
import com.finance.backend.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FinanceRecordService {
    private final FinanceRecordRepository recordRepository;
    private final UserRepository userRepository;

    public Page<FinanceRecordResponse> getRecords(RecordType type, String category, LocalDate from, LocalDate to, Pageable pageable) {
        Specification<FinanceRecord> spec = FinanceRecordSpecification.buildFilter(type, category, from, to);
        return recordRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public FinanceRecordResponse getById(Long id) {
        return toResponse(findActiveOrThrow(id));
    }

    @Transactional
    public FinanceRecordResponse create(CreateRecordRequest request) {
        User currentUser = getCurrentUser();
        FinanceRecord record = FinanceRecord.builder()
            .amount(request.getAmount())
            .type(request.getType())
            .category(request.getCategory().trim())
            .date(request.getDate())
            .notes(request.getNotes())
            .createdBy(currentUser)
            .isDeleted(false)
            .build();
        return toResponse(recordRepository.save(record));
    }

    @Transactional
    public FinanceRecordResponse update(Long id, UpdateRecordRequest request) {
        FinanceRecord record = findActiveOrThrow(id);
        if (request.getAmount() != null) { record.setAmount(request.getAmount()); }
        if (request.getType() != null) { record.setType(request.getType()); }
        if (request.getCategory() != null) { record.setCategory(request.getCategory().trim()); }
        if (request.getDate() != null) { record.setDate(request.getDate()); }
        if (request.getNotes() != null) { record.setNotes(request.getNotes()); }
        return toResponse(recordRepository.save(record));
    }

    @Transactional
    public void softDelete(Long id) {
        FinanceRecord record = findActiveOrThrow(id);
        record.setIsDeleted(true);
        recordRepository.save(record);
    }

    public FinanceRecordResponse toResponse(FinanceRecord r) {
        return FinanceRecordResponse.builder()
            .id(r.getId())
            .amount(r.getAmount())
            .type(r.getType())
            .category(r.getCategory())
            .date(r.getDate())
            .notes(r.getNotes())
            .createdByName(r.getCreatedBy() != null ? r.getCreatedBy().getName() : null)
            .createdAt(r.getCreatedAt())
            .updatedAt(r.getUpdatedAt())
            .build();
    }

    private FinanceRecord findActiveOrThrow(Long id) {
        return recordRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("FinanceRecord", id));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
