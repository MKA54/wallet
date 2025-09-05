package com.example.wallet.service.error;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal amount, UUID id) {
        super("Недостаточно средств на счете " + id + ". Текущий баланс: " + amount);
    }
}