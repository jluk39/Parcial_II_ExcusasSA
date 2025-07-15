package com.excusas.service;

import com.excusas.excepciones.BusinessException;
import com.excusas.model.empleado.Empleado;
import com.excusas.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    public void testCrearEmpleado_Exito() {
        // Arrange
        when(empleadoRepository.findByLegajo(12345)).thenReturn(Optional.empty());
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Empleado empleado = empleadoService.crearEmpleado("Juan Pérez", "juan@empresa.com", 12345);

        // Assert
        assertNotNull(empleado);
        assertEquals("Juan Pérez", empleado.getNombre());
        assertEquals("juan@empresa.com", empleado.getEmail());
        assertEquals(12345, empleado.getLegajo());
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    public void testCrearEmpleado_ErrorLegajoDuplicado() {
        Empleado empleadoExistente = new Empleado("Ana García", "ana@empresa.com", 12345);
        when(empleadoRepository.findByLegajo(12345)).thenReturn(Optional.of(empleadoExistente));
        Exception exception = assertThrows(BusinessException.class,
            () -> empleadoService.crearEmpleado("Juan Pérez", "juan@empresa.com", 12345));
        assertTrue(exception.getMessage().contains("Ya existe un empleado con legajo"));
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    public void testCrearEmpleado_ErrorEmailDuplicado() {
        Empleado empleadoExistente = new Empleado("Ana García", "ana@empresa.com", 54321);
        when(empleadoRepository.findByLegajo(54321)).thenReturn(Optional.empty());
        when(empleadoRepository.findByEmail("ana@empresa.com")).thenReturn(Optional.of(empleadoExistente));
        Exception exception = assertThrows(BusinessException.class,
            () -> empleadoService.crearEmpleado("Juan Pérez", "ana@empresa.com", 54321));
        assertTrue(exception.getMessage().contains("Ya existe un empleado con email"));
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    public void testCrearEmpleado_ErrorDatosInvalidos() {
        Exception exception = assertThrows(BusinessException.class,
            () -> empleadoService.crearEmpleado("", "juan@empresa.com", 12345));
        assertTrue(exception.getMessage().contains("nombre del empleado es obligatorio"));
        exception = assertThrows(BusinessException.class,
            () -> empleadoService.crearEmpleado("Juan", "email-invalido", 12345));
        assertTrue(exception.getMessage().contains("email debe ser válido"));
        exception = assertThrows(BusinessException.class,
            () -> empleadoService.crearEmpleado("Juan", "juan@empresa.com", -1));
        assertTrue(exception.getMessage().contains("legajo debe ser un número positivo"));
    }

    @Test
    public void testBuscarPorLegajo_EmpleadoNoExiste() {
        when(empleadoRepository.findByLegajo(99999)).thenReturn(Optional.empty());
        Optional<Empleado> resultado = empleadoService.buscarPorLegajo(99999);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testBuscarPorLegajo_LegajoInvalido() {
        Exception exception = assertThrows(BusinessException.class,
            () -> empleadoService.buscarPorLegajo(-1));
        assertTrue(exception.getMessage().contains("legajo debe ser un número positivo"));
    }
}