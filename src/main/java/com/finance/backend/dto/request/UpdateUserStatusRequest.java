package com.finance.backend.dto.request;

import com.finance.backend.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    @NotNull(message = "Status is required")
    private UserStatus status;
}
