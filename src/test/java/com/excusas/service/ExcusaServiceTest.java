// src/test/java/com/excusas/service/ExcusaServiceTest.java
package com.excusas.service;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.Trivial;
import com.excusas.repository.ExcusaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExcusaServiceTest {

    @Mock
    private ExcusaRepository excusaRepository;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private MotivoService motivoService;

    @InjectMocks
    private ExcusaService excusaService;

    @Test
    public void testCrearExcusa_Exito() {
        // Arrange
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan.perez@empresa.com", 12345, "trivial", "Motivo válido");
        Empleado empleado = new Empleado("Juan Pérez", "juan.perez@empresa.com", 12345);
        Trivial motivo = new Trivial();

        when(empleadoService.buscarPorLegajo(12345)).thenReturn(Optional.of(empleado));
        when(motivoService.crearMotivo("trivial")).thenReturn(motivo);
        when(excusaRepository.save(any(IExcusa.class))).thenAnswer(invocation -> {
            IExcusa excusa = invocation.getArgument(0);
            excusa.setId(1L);
            return excusa;
        });

        // Act
        ExcusaResponseDTO response = excusaService.crearExcusa(request);

        // Assert
        assertNotNull(response);
        assertEquals("Juan Pérez", response.getEmpleadoNombre());
        assertEquals("Trivial", response.getTipoMotivo());
        verify(excusaRepository, times(1)).save(any(IExcusa.class));
    }

    @Test
    public void testCrearExcusa_ErrorMotivoInvalido() {
        // Arrange
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan.perez@empresa.com", 12345, "inexistente", "Motivo inválido");

        when(motivoService.crearMotivo("inexistente")).thenThrow(new IllegalArgumentException("Tipo de motivo no válido"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> excusaService.crearExcusa(request));
        assertTrue(exception.getMessage().contains("Tipo de motivo no válido"));
    }
}