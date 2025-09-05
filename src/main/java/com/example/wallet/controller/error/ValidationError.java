package com.example.wallet.controller.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidationError {
    private String fieldName;
    private String message;

    public ValidationError(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}