package com.example.wallet.controller.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse {
    private List<ValidationError> validationErrors;

    public ValidationErrorResponse(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}