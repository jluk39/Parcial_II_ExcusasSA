// src/test/java/com/excusas/service/ProntuarioServiceTest.java
package com.excusas.service;

import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.motivo.IMotivoExcusa;
import com.excusas.model.excusa.motivo.Trivial;
import com.excusas.model.excusa.motivo.Moderada;
import com.excusas.model.excusa.motivo.Compleja;
import com.excusas.repository.ExcusaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProntuarioServiceTest {

    @Mock
    private ExcusaRepository excusaRepository;

    @InjectMocks
    private ProntuarioService prontuarioService;

    @Test
    public void testObtenerTodosProntuarios_SoloExcusasProcesadas() {
        // Arrange
        Empleado empleado1 = new Empleado("Juan Pérez", "juan@empresa.com", 12345);
        Empleado empleado2 = new Empleado("Ana García", "ana@empresa.com", 54321);

        IMotivoExcusa trivial = new Trivial();
        IMotivoExcusa moderada = new Moderada() {};

        Excusa excusaProcesada = new Excusa(trivial, empleado1);
        excusaProcesada.setId(1L);
        excusaProcesada.setEstado("PROCESADA");

        Excusa excusaRechazada = new Excusa(moderada, empleado2);
        excusaRechazada.setId(2L);
        excusaRechazada.setEstado("RECHAZADA");

        Excusa excusaPendiente = new Excusa(trivial, empleado1);
        excusaPendiente.setId(3L);
        excusaPendiente.setEstado("PENDIENTE");

        when(excusaRepository.findAll()).thenReturn(Arrays.asList(
            excusaProcesada, excusaRechazada, excusaPendiente
        ));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertNotNull(prontuarios);
        assertEquals(2, prontuarios.size()); // Solo las procesadas y rechazadas

        List<String> estados = prontuarios.stream()
            .map(ExcusaResponseDTO::getEstado)
            .toList();
        assertTrue(estados.contains("PROCESADA"));
        assertTrue(estados.contains("RECHAZADA"));
        assertFalse(estados.contains("PENDIENTE"));
    }

    @Test
    public void testObtenerProntuarioPorEmpleado() {
        // Arrange
        Empleado empleado = new Empleado("Juan Pérez", "juan@empresa.com", 12345);
        IMotivoExcusa trivial = new Trivial();

        Excusa excusaProcesada = new Excusa(trivial, empleado);
        excusaProcesada.setId(1L);
        excusaProcesada.setEstado("PROCESADA");

        when(excusaRepository.findByEmpleadoNombre("Juan Pérez"))
            .thenReturn(Arrays.asList(excusaProcesada));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerProntuarioPorEmpleado("Juan Pérez");

        // Assert
        assertNotNull(prontuarios);
        assertEquals(1, prontuarios.size());
        assertEquals("Juan Pérez", prontuarios.get(0).getEmpleadoNombre());
        assertEquals("PROCESADA", prontuarios.get(0).getEstado());
    }

    @Test
    public void testDeterminarTipoMotivo_Trivial() {
        // Arrange
        Empleado empleado = new Empleado("Test", "test@empresa.com", 123);
        IMotivoExcusa motivo = new Trivial();
        Excusa excusa = new Excusa(motivo, empleado);
        excusa.setId(1L);
        excusa.setEstado("PROCESADA");

        when(excusaRepository.findAll()).thenReturn(Arrays.asList(excusa));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertEquals(1, prontuarios.size());
        assertEquals("Trivial", prontuarios.get(0).getTipoMotivo());
    }

    @Test
    public void testDeterminarTipoMotivo_Moderada() {
        // Arrange
        Empleado empleado = new Empleado("Test", "test@empresa.com", 123);
        IMotivoExcusa motivo = new Moderada() {};
        Excusa excusa = new Excusa(motivo, empleado);
        excusa.setId(1L);
        excusa.setEstado("PROCESADA");

        when(excusaRepository.findAll()).thenReturn(Arrays.asList(excusa));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertEquals(1, prontuarios.size());
        assertEquals("Moderada", prontuarios.get(0).getTipoMotivo());
    }

    @Test
    public void testDeterminarTipoMotivo_Compleja() {
        // Arrange
        Empleado empleado = new Empleado("Test", "test@empresa.com", 123);
        IMotivoExcusa motivo = new Compleja();
        Excusa excusa = new Excusa(motivo, empleado);
        excusa.setId(1L);
        excusa.setEstado("PROCESADA");

        when(excusaRepository.findAll()).thenReturn(Arrays.asList(excusa));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertEquals(1, prontuarios.size());
        assertEquals("Compleja", prontuarios.get(0).getTipoMotivo());
    }

    @Test
    public void testObtenerTodosProntuarios_ListaVacia() {
        // Arrange
        when(excusaRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertNotNull(prontuarios);
        assertTrue(prontuarios.isEmpty());
    }

    @Test
    public void testObtenerProntuarioPorEmpleado_EmpleadoInexistente() {
        // Arrange
        when(excusaRepository.findByEmpleadoNombre("Empleado Inexistente"))
            .thenReturn(Arrays.asList());

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerProntuarioPorEmpleado("Empleado Inexistente");

        // Assert
        assertNotNull(prontuarios);
        assertTrue(prontuarios.isEmpty());
    }

    @Test
    public void testConvertirAProntuarioDTO_CamposCompletos() {
        // Arrange
        Empleado empleado = new Empleado("Juan Pérez", "juan@empresa.com", 12345);
        IMotivoExcusa motivo = new Trivial();

        Excusa excusa = new Excusa(motivo, empleado);
        excusa.setId(1L);
        excusa.setEstado("PROCESADA");
        excusa.setEncargadoProcesador("Recepcionista");

        when(excusaRepository.findAll()).thenReturn(Arrays.asList(excusa));

        // Act
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();

        // Assert
        assertEquals(1, prontuarios.size());
        ExcusaResponseDTO dto = prontuarios.get(0);

        assertNotNull(dto.getId());
        assertEquals("Juan Pérez", dto.getEmpleadoNombre());
        assertEquals("Trivial", dto.getTipoMotivo());
        assertNotNull(dto.getDescripcion());
        assertEquals("PROCESADA", dto.getEstado());
        assertNotNull(dto.getFechaCreacion());
    }
}