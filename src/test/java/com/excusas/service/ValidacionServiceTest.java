// src/test/java/com/excusas/service/ValidacionServiceTest.java
package com.excusas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidacionServiceTest {

    @InjectMocks
    private ValidacionService validacionService;

    @Test
    public void testEsEmailValido_EmailsCorrectos() {
        assertTrue(validacionService.esEmailValido("usuario@empresa.com"));
        assertTrue(validacionService.esEmailValido("test.email+tag@dominio.org"));
        assertTrue(validacionService.esEmailValido("nombre.apellido@sub.dominio.com"));
    }

    @Test
    public void testEsEmailValido_EmailsIncorrectos() {
        assertFalse(validacionService.esEmailValido(null));
        assertFalse(validacionService.esEmailValido(""));
        assertFalse(validacionService.esEmailValido("email-sin-arroba"));
        assertFalse(validacionService.esEmailValido("@dominio.com"));
        assertFalse(validacionService.esEmailValido("usuario@"));
    }

    @Test
    public void testEsNombreValido_NombresCorrectos() {
        assertTrue(validacionService.esNombreValido("Juan Pérez"));
        assertTrue(validacionService.esNombreValido("María José García"));
        assertTrue(validacionService.esNombreValido("José"));
        assertTrue(validacionService.esNombreValido("Ana"));
    }

    @Test
    public void testEsNombreValido_NombresIncorrectos() {
        assertFalse(validacionService.esNombreValido(null));
        assertFalse(validacionService.esNombreValido(""));
        assertFalse(validacionService.esNombreValido("A")); // Muy corto
        assertFalse(validacionService.esNombreValido("Juan123")); // Con números
        assertFalse(validacionService.esNombreValido("Juan@Pérez")); // Con caracteres especiales
    }

    @Test
    public void testEsLegajoValido_LegajosCorrectos() {
        assertTrue(validacionService.esLegajoValido(1));
        assertTrue(validacionService.esLegajoValido(12345));
        assertTrue(validacionService.esLegajoValido(999999));
    }

    @Test
    public void testEsLegajoValido_LegajosIncorrectos() {
        assertFalse(validacionService.esLegajoValido(null));
        assertFalse(validacionService.esLegajoValido(0));
        assertFalse(validacionService.esLegajoValido(-1));
        assertFalse(validacionService.esLegajoValido(1000000)); // Muy grande
    }

    @Test
    public void testEsDescripcionValida_DescripcionesCorrectas() {
        assertTrue(validacionService.esDescripcionValida("Esta es una descripción válida de al menos 10 caracteres"));
        assertTrue(validacionService.esDescripcionValida("Descripción mínima"));
    }

    @Test
    public void testEsDescripcionValida_DescripcionesIncorrectas() {
        assertFalse(validacionService.esDescripcionValida(null));
        assertFalse(validacionService.esDescripcionValida(""));
        assertFalse(validacionService.esDescripcionValida("Corta")); // Muy corta

        // Descripción muy larga (más de 500 caracteres)
        String descripcionLarga = "A".repeat(501);
        assertFalse(validacionService.esDescripcionValida(descripcionLarga));
    }
}