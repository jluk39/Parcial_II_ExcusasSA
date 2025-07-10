// src/test/java/com/excusas/validation/NegocioValidationTest.java
package com.excusas.validation;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.service.ExcusaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class NegocioValidationTest {

    @Autowired
    private ExcusaService excusaService;

    @Test
    public void testValidacion_RequestNulo() {
        Exception exception = assertThrows(RuntimeException.class, () ->
            excusaService.crearExcusa(null));

        assertTrue(exception.getMessage().contains("request no puede ser null"));
    }

    @Test
    public void testValidacion_DescripcionObligatoria() {
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Test Usuario", "test@empresa.com", 99999, "trivial", null
        );

        Exception exception = assertThrows(RuntimeException.class, () ->
            excusaService.crearExcusa(request));

        assertTrue(exception.getMessage().contains("descripción es obligatoria"));
    }

    @Test
    public void testValidacion_DescripcionMuyLarga() {
        String descripcionLarga = "A".repeat(501);
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Test Usuario", "test@empresa.com", 99999, "trivial", descripcionLarga
        );

        Exception exception = assertThrows(RuntimeException.class, () ->
            excusaService.crearExcusa(request));

        assertTrue(exception.getMessage().contains("no puede exceder 500 caracteres"));
    }

    @Test
    public void testValidacion_TipoMotivoInvalido() {
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Test Usuario", "test@empresa.com", 99999, "tipo_inexistente",
            "Descripción válida de prueba"
        );

        Exception exception = assertThrows(RuntimeException.class, () ->
            excusaService.crearExcusa(request));

        assertTrue(exception.getMessage().contains("Tipo de motivo no válido"));
    }

    @Test
    public void testValidacion_EmpleadoDuplicado() {
        // Crear primer empleado
        ExcusaRequestDTO request1 = new ExcusaRequestDTO(
            "Juan Duplicado", "juan1@empresa.com", 77777, "trivial",
            "Primera excusa del empleado"
        );
        excusaService.crearExcusa(request1);

        // Intentar crear segundo empleado con mismo legajo pero diferente nombre
        ExcusaRequestDTO request2 = new ExcusaRequestDTO(
            "Pedro Diferente", "pedro@empresa.com", 77777, "trivial",
            "Segunda excusa con legajo duplicado"
        );

        // Debe usar el empleado existente, no crear uno nuevo
        assertDoesNotThrow(() -> excusaService.crearExcusa(request2));
    }
}