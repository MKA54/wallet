package com.example.wallet.service.error;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(UUID id) {
        super("Кошелек не найден: " + id);
    }
}