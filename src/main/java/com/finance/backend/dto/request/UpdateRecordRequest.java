package com.finance.backend.dto.request;

import com.finance.backend.enums.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateRecordRequest {
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;
    private RecordType type;
    @Size(max = 100)
    private String category;
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;
    @Size(max = 500)
    private String notes;
}
