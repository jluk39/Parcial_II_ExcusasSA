package com.excusas.service;

import com.excusas.model.excusa.motivo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MotivoServiceTest {

    @InjectMocks
    private MotivoService motivoService;

    @Test
    public void testCrearMotivo_Trivial() {
        // Act
        IMotivoExcusa motivo = motivoService.crearMotivo("trivial");

        // Assert
        assertNotNull(motivo);
        assertTrue(motivo instanceof Trivial);
        assertTrue(motivo.esTrivial());
        assertFalse(motivo.esModerada());
    }

    @Test
    public void testCrearMotivo_Moderada() {
        // Act
        IMotivoExcusa motivo = motivoService.crearMotivo("moderada");

        // Assert
        assertNotNull(motivo);
        assertTrue(motivo.esModerada());
        assertFalse(motivo.esTrivial());
    }

    @Test
    public void testCrearMotivo_Compleja() {
        // Act
        IMotivoExcusa motivo = motivoService.crearMotivo("compleja");

        // Assert
        assertNotNull(motivo);
        assertTrue(motivo instanceof Compleja);
        assertTrue(motivo.esCompleja());
    }

    @Test
    public void testCrearMotivo_TipoInvalido() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> motivoService.crearMotivo("inexistente"));

        assertTrue(exception.getMessage().contains("Tipo de motivo no válido"));
    }

    @Test
    public void testCrearMotivo_TipoNulo() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> motivoService.crearMotivo(null));

        assertTrue(exception.getMessage().contains("tipo de motivo es obligatorio"));
    }

    @Test
    public void testObtenerTiposValidos() {
        // Act
        List<String> tipos = motivoService.obtenerTiposValidos();

        // Assert
        assertNotNull(tipos);
        assertTrue(tipos.contains("trivial"));
        assertTrue(tipos.contains("moderada"));
        assertTrue(tipos.contains("compleja"));
        assertTrue(tipos.size() >= 3);
    }

    @Test
    public void testEsTipoValido() {
        // Assert
        assertTrue(motivoService.esTipoValido("trivial"));
        assertTrue(motivoService.esTipoValido("MODERADA"));
        assertFalse(motivoService.esTipoValido("inexistente"));
        assertFalse(motivoService.esTipoValido(""));
    }
}