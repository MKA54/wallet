package com.example.wallet.controller.error;

import com.example.wallet.service.error.InsufficientFundsException;
import com.example.wallet.service.error.WalletNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ValidationErrorResponse handleNotFound(WalletNotFoundException ex) {
        return new ValidationErrorResponse(
                Collections.singletonList(
                        new ValidationError("wallet", ex.getMessage())
                )
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ValidationErrorResponse handleNotFound(InsufficientFundsException ex) {
        return new ValidationErrorResponse(
                Collections.singletonList(
                        new ValidationError("wallet", ex.getMessage())
                )
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ResponseEntity<>("JSON не читается: " + e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleValidationErrors(Exception ex) {
        List<ValidationError> errors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException ave) {
            errors = extractErrorsFromBindingResult(ave.getBindingResult());
        } else if (ex instanceof ConstraintViolationException cve) {
            errors = extractErrorsFromConstraintViolations(cve.getConstraintViolations());
        }

        return new ValidationErrorResponse(errors);
    }

    private List<ValidationError> extractErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(this::convertToValidationError)
                .collect(Collectors.toList());
    }

    private List<ValidationError> extractErrorsFromConstraintViolations(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> new ValidationError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());
    }

    private ValidationError convertToValidationError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return new ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }
        return new ValidationError(
                "global",
                error.getDefaultMessage()
        );
    }
}
