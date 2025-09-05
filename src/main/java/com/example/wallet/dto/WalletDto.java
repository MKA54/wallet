package com.example.wallet.dto;

import com.example.wallet.utils.OperationTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(
        @Schema(description = "ИД", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(groups = Update.class, message = "ИД не должен быть null")
        UUID id,

        @NotNull(groups = Update.class, message = "Тип операции не должен быть null")
        @Schema(description = "Тип операции", example = "DEPOSIT")
        OperationTypes operationType,
        @NotNull(groups = Update.class, message = "Сумма не должна null")
        @PositiveOrZero(groups = Update.class, message = "Сумма не должна быть отрицательной")
        @Digits(groups = Update.class, integer = 19, fraction = 2, message = "Неверный формат числа")
        @Schema(description = "Сумма", example = "155")
        BigDecimal amount) {
}