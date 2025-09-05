package com.example.wallet.service.error;

import org.springframework.dao.DataAccessException;

import java.io.Serial;

public class UniqueConstraintViolationException extends DataAccessException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UniqueConstraintViolationException(String detailMessage) {
        super(detailMessage);
    }

    public UniqueConstraintViolationException(String detailMessage, Throwable rootCause) {
        super(detailMessage, rootCause);
    }
}