// src/test/java/com/excusas/service/ExcusaServiceTest.java
package com.excusas.service;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.excepciones.BusinessException;
import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.Excusa;
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
        when(excusaRepository.save(any(Excusa.class))).thenAnswer(invocation -> {
            Excusa excusa = invocation.getArgument(0);
            excusa.setId(1L);
            return excusa;
        });

        // Act
        ExcusaResponseDTO response = excusaService.crearExcusa(request);

        // Assert
        assertNotNull(response);
        assertEquals("Juan Pérez", response.getEmpleadoNombre());
        assertEquals("Trivial", response.getTipoMotivo());
        verify(excusaRepository, times(1)).save(any(Excusa.class));
    }

    @Test
    public void testCrearExcusa_ErrorRequestNulo() {
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(null));
        assertTrue(exception.getMessage().contains("request no puede ser null"));
    }

    @Test
    public void testCrearExcusa_ErrorDescripcionObligatoria() {
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan@empresa.com", 12345, "trivial", "");
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(request));
        assertTrue(exception.getMessage().contains("descripción es obligatoria"));
    }

    @Test
    public void testCrearExcusa_ErrorDescripcionMuyLarga() {
        String descripcionLarga = "A".repeat(501);
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan@empresa.com", 12345, "trivial", descripcionLarga);
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(request));
        assertTrue(exception.getMessage().contains("no puede exceder 500 caracteres"));
    }

    @Test
    public void testCrearExcusa_ErrorTipoMotivoObligatorio() {
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan@empresa.com", 12345, "", "Motivo válido");
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(request));
        assertTrue(exception.getMessage().contains("tipo de motivo es obligatorio"));
    }

    @Test
    public void testCrearExcusa_ErrorTipoMotivoInvalido() {
        ExcusaRequestDTO request = new ExcusaRequestDTO("Juan Pérez", "juan@empresa.com", 12345, "inexistente", "Motivo inválido");
        when(motivoService.crearMotivo("inexistente")).thenThrow(new IllegalArgumentException("Tipo de motivo no válido"));
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(request));
        assertTrue(exception.getMessage().contains("Tipo de motivo no válido"));
    }

    @Test
    public void testCrearExcusa_ErrorEmpleadoDatosInvalidos() {
        ExcusaRequestDTO requestSinNombre = new ExcusaRequestDTO("", "juan@empresa.com", 12345, "trivial", "Motivo válido");
        Exception exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(requestSinNombre));
        assertTrue(exception.getMessage().contains("nombre del empleado es obligatorio"));
        ExcusaRequestDTO requestSinEmail = new ExcusaRequestDTO("Juan Pérez", "", 12345, "trivial", "Motivo válido");
        exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(requestSinEmail));
        assertTrue(exception.getMessage().contains("email del empleado debe ser válido"));
        ExcusaRequestDTO requestLegajoInvalido = new ExcusaRequestDTO("Juan Pérez", "juan@empresa.com", -1, "trivial", "Motivo válido");
        exception = assertThrows(BusinessException.class,
            () -> excusaService.crearExcusa(requestLegajoInvalido));
        assertTrue(exception.getMessage().contains("legajo del empleado es obligatorio"));
    }
}