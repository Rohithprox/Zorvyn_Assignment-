package com.finance.backend.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategorySummaryResponse {
    private String category;
    private BigDecimal total;
    private Long count;
}
