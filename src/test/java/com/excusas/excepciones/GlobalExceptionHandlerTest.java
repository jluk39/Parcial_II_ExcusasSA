package com.excusas.excepciones;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Error de negocio");
        ResponseEntity<Map<String, String>> response = handler.handleBusinessException(ex);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error de negocio", response.getBody().get("error"));
    }

    @Test
    public void testHandleGeneralException() {
        Exception ex = new Exception("Error general");
        ResponseEntity<Map<String, String>> response = handler.handleGeneralException(ex);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error general", response.getBody().get("error"));
    }
}

