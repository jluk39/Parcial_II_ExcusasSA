package com.excusas.integration;

import com.excusas.dto.EmpleadoRequestDTO;
import com.excusas.dto.EmpleadoResponseDTO;
import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb_endpoints"
})
public class NewEndpointsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCrearYObtenerEmpleado() {
        // Crear empleado
        EmpleadoRequestDTO request = new EmpleadoRequestDTO("Ana García", "ana@empresa.com", 98765);

        ResponseEntity<EmpleadoResponseDTO> createResponse = restTemplate.postForEntity(
            "/api/empleados",
            request,
            EmpleadoResponseDTO.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        EmpleadoResponseDTO empleadoCreado = createResponse.getBody();

        assertEquals("Ana García", empleadoCreado.getNombre());
        assertEquals("ana@empresa.com", empleadoCreado.getEmail());
        assertEquals(98765, empleadoCreado.getLegajo());

        // Obtener todos los empleados
        ResponseEntity<List> getAllResponse = restTemplate.getForEntity(
            "/api/empleados",
            List.class
        );

        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertNotNull(getAllResponse.getBody());
        assertTrue(getAllResponse.getBody().size() >= 1);
    }

    @Test
    public void testObtenerExcusasPorLegajo() {
        // Crear empleado y excusa
        crearEmpleadoYExcusa("Pedro López", "pedro@empresa.com", 54321, "trivial");

        // Obtener excusas por legajo
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/excusas/legajo/54321",
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 1);
    }

    @Test
    public void testBuscarExcusasConFiltros() {
        // Crear empleado y excusa
        crearEmpleadoYExcusa("María Rodríguez", "maria@empresa.com", 11111, "moderada");

        // Buscar con filtros de fecha
        String fechaHoy = java.time.LocalDate.now().toString();
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/excusas/busqueda?legajo=11111&fechaDesde=" + fechaHoy + "&fechaHasta=" + fechaHoy,
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testObtenerExcusasRechazadas() {
        // Obtener excusas rechazadas (puede estar vacío inicialmente)
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/excusas/rechazadas",
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // No verificamos el tamaño porque depende del estado inicial
    }

    @Test
    public void testEliminarExcusasSinFechaLimite() {
        // Intentar eliminar sin fecha límite debe devolver error 400
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/excusas/eliminar",
            org.springframework.http.HttpMethod.DELETE,
            null,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody()).contains("El parámetro fechaLimite es obligatorio");
    }

    @Test
    public void testEliminarExcusasConFechaLimite() {
        // Eliminar excusas anteriores a ayer (no debería eliminar nada reciente)
        String fechaAyer = java.time.LocalDate.now().minusDays(1).toString();

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/excusas/eliminar?fechaLimite=" + fechaAyer,
            org.springframework.http.HttpMethod.DELETE,
            null,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).contains("Se eliminaron");
    }

    @Test
    public void testObtenerExcusasPorEmpleadoNombre() {
        // Crear empleado y excusa
        crearEmpleadoYExcusa("Luis Martínez", "luis@empresa.com", 22222, "compleja");

        // Obtener excusas por nombre de empleado
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/excusas/empleado/Luis Martínez",
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testValidacionesBadRequest() {
        // Test crear empleado con datos inválidos
        EmpleadoRequestDTO requestInvalido = new EmpleadoRequestDTO("", "email-invalido", -1);

        ResponseEntity<EmpleadoResponseDTO> response = restTemplate.postForEntity(
            "/api/empleados",
            requestInvalido,
            EmpleadoResponseDTO.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testEndpointsProntuarios() {
        // Verificar que el endpoint de prontuarios existe
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/prontuarios",
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testEndpointsEncargados() {
        // Verificar que el endpoint de encargados existe
        ResponseEntity<List> response = restTemplate.getForEntity(
            "/api/encargados",
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    private void crearEmpleadoYExcusa(String nombre, String email, Integer legajo, String tipoMotivo) {
        // Crear excusa que automáticamente crea el empleado si no existe
        ExcusaRequestDTO excusaRequest = new ExcusaRequestDTO(
            nombre, email, legajo, tipoMotivo, "Descripción de prueba"
        );

        restTemplate.postForEntity(
            "/api/excusas",
            excusaRequest,
            ExcusaResponseDTO.class
        );
    }
}
