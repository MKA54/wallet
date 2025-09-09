package com.example.wallet.controller.error;

import com.example.wallet.service.error.UniqueConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ErrorHandlingControllerAdviceDB {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleDataIntegrityError(DataIntegrityViolationException ex) {
        List<ValidationError> errors = Collections.singletonList(
                new ValidationError("database", "Нарушение целостности данных: " + ex.getMostSpecificCause().getMessage())
        );
        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ValidationErrorResponse handleOptimisticLockError(OptimisticLockingFailureException ex) {
        List<ValidationError> errors = Collections.singletonList(
                new ValidationError("lock", "Конфликт версий записи: " + ex.getMostSpecificCause().getMessage())
        );
        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ValidationErrorResponse handleUniqueConstraintError(UniqueConstraintViolationException ex) {
        List<ValidationError> errors = Collections.singletonList(
                new ValidationError("unique", "Нарушение уникальности: " + ex.getMessage())
        );
        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(TransientDataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ValidationErrorResponse handleTransientDatabaseError(TransientDataAccessException ex) {
        List<ValidationError> errors = Collections.singletonList(
                new ValidationError("database", "Временная ошибка доступа к базе данных: " + ex.getMessage())
        );
        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ValidationErrorResponse handleDatabaseError(DataAccessException ex) {
        String errorMessage = ex.getMessage();

        if (isDeadlockError(ex)) {
            return handleDeadlockError(ex);
        }

        return new ValidationErrorResponse(Collections.singletonList(
                new ValidationError("database", "Ошибка доступа к базе данных: " + errorMessage)
        ));
    }

    private boolean isDeadlockError(DataAccessException ex) {
        Throwable rootCause = ex.getMostSpecificCause();
        return rootCause.getMessage().contains("deadlock") || rootCause.getMessage().contains("lock wait timeout");
    }

    private ValidationErrorResponse handleDeadlockError(DataAccessException ex) {
        return new ValidationErrorResponse(Collections.singletonList(
                new ValidationError("database", "Возникла взаимоблокировка: " + ex.getMessage())
        ));
    }
}