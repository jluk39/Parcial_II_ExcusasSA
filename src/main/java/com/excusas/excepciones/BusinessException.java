package com.excusas.excepciones;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

