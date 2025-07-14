// src/test/java/com/excusas/integration/ExcusaIntegrationTest.java
package com.excusas.integration;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ExcusaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearYObtenerExcusaCompleto() {
        // Paso 1: Crear una excusa
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Juan Pérez",
            "juan.perez@empresa.com",
            12345,
            "trivial",
            "Se me hizo tarde por el tráfico"
        );

        ResponseEntity<ExcusaResponseDTO> createResponse = restTemplate.postForEntity(
            "/api/excusas",
            request,
            ExcusaResponseDTO.class
        );

        // Verificar que la creación fue exitosa
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        ExcusaResponseDTO excusaCreada = createResponse.getBody();

        // Verificar datos de la excusa creada
        assertNotNull(excusaCreada.getId());
        assertEquals("Juan Pérez", excusaCreada.getEmpleadoNombre());
        assertEquals("Trivial", excusaCreada.getTipoMotivo());
        assertNotNull(excusaCreada.getEstado());
        assertNotNull(excusaCreada.getFechaCreacion());

        // Paso 2: Obtener la excusa por ID
        ResponseEntity<ExcusaResponseDTO> getResponse = restTemplate.getForEntity(
            "/api/excusas/" + excusaCreada.getId(),
            ExcusaResponseDTO.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());

        ExcusaResponseDTO excusaObtenida = getResponse.getBody();
        assertEquals(excusaCreada.getId(), excusaObtenida.getId());
        assertEquals(excusaCreada.getEmpleadoNombre(), excusaObtenida.getEmpleadoNombre());

        // Paso 3: Verificar que aparece en la lista completa
        ResponseEntity<List> allExcusasResponse = restTemplate.getForEntity(
            "/api/excusas",
            List.class
        );

        assertEquals(HttpStatus.OK, allExcusasResponse.getStatusCode());
        assertNotNull(allExcusasResponse.getBody());
        assertTrue(allExcusasResponse.getBody().size() >= 1);

        // Paso 4: Buscar por empleado
        ResponseEntity<List> empleadoExcusasResponse = restTemplate.getForEntity(
            "/api/excusas/empleado/Juan Pérez",
            List.class
        );

        assertEquals(HttpStatus.OK, empleadoExcusasResponse.getStatusCode());
        assertNotNull(empleadoExcusasResponse.getBody());
        assertTrue(empleadoExcusasResponse.getBody().size() >= 1);
    }

    @Test
    public void testCrearMultiplesExcusasYVerificarPersistencia() {
        // Crear múltiples excusas con diferentes motivos
        ExcusaRequestDTO[] requests = {
                new ExcusaRequestDTO("Ana García", "ana@empresa.com", 11111, "trivial", "Llegué tarde"),
                new ExcusaRequestDTO("Luis Rodríguez", "luis@empresa.com", 22222, "moderada", "Problema familiar"),
                new ExcusaRequestDTO("María López", "maria@empresa.com", 33333, "compleja", "Emergencia médica")
        };

        // Crear todas las excusas
        for (ExcusaRequestDTO request : requests) {
            ResponseEntity<ExcusaResponseDTO> response = restTemplate.postForEntity(
                "/api/excusas",
                request,
                ExcusaResponseDTO.class
            );
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        // Verificar que todas fueron persistidas
        ResponseEntity<List> allExcusasResponse = restTemplate.getForEntity(
            "/api/excusas",
            List.class
        );

        assertEquals(HttpStatus.OK, allExcusasResponse.getStatusCode());
        assertTrue(allExcusasResponse.getBody().size() >= 3);
    }

    @Test
    public void testErrorHandling() {
        // Probar con tipo de motivo inválido
        ExcusaRequestDTO requestInvalido = new ExcusaRequestDTO(
            "Test Usuario",
            "test@empresa.com",
            99999,
            "inexistente",
            "Motivo que no existe"
        );

        ResponseEntity<ExcusaResponseDTO> response = restTemplate.postForEntity(
            "/api/excusas",
            requestInvalido,
            ExcusaResponseDTO.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testObtenerExcusaInexistente() {
        // Probar obtener excusa que no existe
        ResponseEntity<ExcusaResponseDTO> response = restTemplate.getForEntity(
            "/api/excusas/99999",
            ExcusaResponseDTO.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFormatoRespuestaCompleto() {
        // Crear excusa y verificar que el formato de respuesta sea correcto
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Pedro Sánchez",
            "pedro@empresa.com",
            54321,
            "moderada",
            "Cita médica urgente"
        );

        ResponseEntity<ExcusaResponseDTO> response = restTemplate.postForEntity(
            "/api/excusas",
            request,
            ExcusaResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ExcusaResponseDTO excusa = response.getBody();

        // Verificar que todos los campos esperados estén presentes
        assertNotNull(excusa.getId(), "ID no debe ser null");
        assertNotNull(excusa.getEmpleadoNombre(), "Nombre empleado no debe ser null");
        assertNotNull(excusa.getTipoMotivo(), "Tipo motivo no debe ser null");
        assertNotNull(excusa.getDescripcion(), "Descripción no debe ser null");
        assertNotNull(excusa.getEstado(), "Estado no debe ser null");

        // Verificar formato de los datos
        assertTrue(excusa.getId() > 0, "ID debe ser mayor que 0");
        assertEquals("Pedro Sánchez", excusa.getEmpleadoNombre());
        assertEquals("Moderada", excusa.getTipoMotivo());
        assertTrue(excusa.getDescripcion().length() > 0, "Descripción debe tener contenido");
    }
}