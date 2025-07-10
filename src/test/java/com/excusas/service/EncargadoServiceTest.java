package com.excusas.service;

import com.excusas.dto.EncargadoStatusDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EncargadoServiceTest {

    @InjectMocks
    private EncargadoService encargadoService;

    @Test
    public void testObtenerEstadoCadena() {
        // Act
        List<EncargadoStatusDTO> estado = encargadoService.obtenerEstadoCadena();

        // Assert
        assertNotNull(estado);
        assertFalse(estado.isEmpty());

        // Verificar que contiene los encargados esperados
        List<String> nombres = estado.stream().map(EncargadoStatusDTO::getNombre).toList();
        assertTrue(nombres.contains("Jeremias")); // Recepcionista
        assertTrue(nombres.contains("Luis")); // Supervisor
        assertTrue(nombres.contains("Roberto")); // Gerente
        assertTrue(nombres.contains("Romina")); // CEO
    }

    @Test
    public void testCambiarModo_Exito() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> encargadoService.cambiarModo("jeremias", "vago"));
        assertDoesNotThrow(() -> encargadoService.cambiarModo("luis", "productivo"));
        assertDoesNotThrow(() -> encargadoService.cambiarModo("roberto", "normal"));
    }

    @Test
    public void testCambiarModo_EncargadoInexistente() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> encargadoService.cambiarModo("inexistente", "normal"));

        assertTrue(exception.getMessage().contains("Encargado no encontrado"));
    }

    @Test
    public void testCambiarModo_ModoInvalido() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> encargadoService.cambiarModo("jeremias", "modo_inexistente"));

        assertTrue(exception.getMessage().contains("Modo no válido"));
    }

    @Test
    public void testVerificarCargosCorrectos() {
        // Act
        List<EncargadoStatusDTO> estado = encargadoService.obtenerEstadoCadena();

        // Assert - Verificar que los cargos se asignan correctamente
        EncargadoStatusDTO recepcionista = estado.stream()
            .filter(e -> e.getNombre().equals("Jeremias"))
            .findFirst().orElse(null);

        assertNotNull(recepcionista);
        assertEquals("Recepcionista", recepcionista.getCargo());
        assertTrue(recepcionista.isActivo());
    }
}
