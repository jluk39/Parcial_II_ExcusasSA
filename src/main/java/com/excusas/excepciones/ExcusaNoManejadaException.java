package com.excusas.excepciones;

public class ExcusaNoManejadaException extends RuntimeException {
    public ExcusaNoManejadaException(String mensaje) {
        super(mensaje);
    }
}