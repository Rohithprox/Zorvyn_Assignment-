package com.finance.backend.dto.request;

import com.finance.backend.enums.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateRecordRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private RecordType type;
    @NotBlank(message = "Category is required")
    @Size(max = 100)
    private String category;
    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
