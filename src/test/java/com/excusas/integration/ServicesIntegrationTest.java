// src/test/java/com/excusas/integration/ServicesIntegrationTest.java
package com.excusas.integration;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ServicesIntegrationTest {

    @Autowired
    private ExcusaService excusaService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private MotivoService motivoService;

    @Autowired
    private EncargadoService encargadoService;

    @Autowired
    private ProntuarioService prontuarioService;

    @Test
    public void testFlujoCompletoCreacionExcusa() {
        // Crear excusa que será procesada por cadena
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Carlos Mendoza",
            "carlos@empresa.com",
            54321,
            "trivial",
            "Llegué tarde por el tráfico, disculpen la demora"
        );

        // Crear excusa
        ExcusaResponseDTO excusaCreada = excusaService.crearExcusa(request);

        // Verificar que se creó correctamente
        assertNotNull(excusaCreada);
        assertEquals("Carlos Mendoza", excusaCreada.getEmpleadoNombre());
        assertEquals("Trivial", excusaCreada.getTipoMotivo());

        // Verificar que el empleado se registró
        assertTrue(empleadoService.buscarPorLegajo(54321).isPresent());

        // Verificar que aparece en prontuarios
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();
        assertTrue(prontuarios.stream().anyMatch(p ->
            p.getEmpleadoNombre().equals("Carlos Mendoza")));
    }

    @Test
    public void testIntegracionEncargadosYExcusas() {
        // Verificar estado inicial de encargados
        var estadoEncargados = encargadoService.obtenerEstadoCadena();
        assertFalse(estadoEncargados.isEmpty());

        // Cambiar modo de un encargado
        encargadoService.cambiarModo("jeremias", "vago");

        // Crear excusa que será procesada por recepcionista
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Laura Vega",
            "laura@empresa.com",
            98765,
            "trivial",
            "Se me hizo tarde porque no sonó el despertador"
        );

        // Procesar excusa
        ExcusaResponseDTO excusa = excusaService.crearExcusa(request);

        // Verificar procesamiento
        assertNotNull(excusa);
        assertNotNull(excusa.getEstado());
    }

    @Test
    public void testValidacionesNegocioIntegradas() {
        // Test con datos inválidos que deben ser rechazados
        ExcusaRequestDTO requestInvalido = new ExcusaRequestDTO(
            "", // Nombre vacío
            "email-invalido", // Email sin @
            -1, // Legajo negativo
            "tipo-inexistente", // Tipo de motivo inválido
            "Corta" // Descripción muy corta
        );

        // Debe fallar por validaciones de negocio
        assertThrows(RuntimeException.class, () ->
            excusaService.crearExcusa(requestInvalido));
    }

    @Test
    public void testConsistenciaEntreRepositorios() {
        // Crear empleado y excusa
        ExcusaRequestDTO request = new ExcusaRequestDTO(
            "Roberto Silva",
            "roberto@empresa.com",
            11111,
            "moderada",
            "Tuve una emergencia familiar que requirió mi atención inmediata"
        );

        excusaService.crearExcusa(request);

        // Verificar consistencia: empleado existe
        assertTrue(empleadoService.buscarPorLegajo(11111).isPresent());

        // Verificar consistencia: excusa existe
        List<ExcusaResponseDTO> excusasEmpleado =
            excusaService.obtenerExcusasPorEmpleado("Roberto Silva");
        assertFalse(excusasEmpleado.isEmpty());

        // Verificar consistencia: aparece en prontuarios
        List<ExcusaResponseDTO> prontuarios =
            prontuarioService.obtenerProntuarioPorEmpleado("Roberto Silva");
        assertEquals(excusasEmpleado.size(), prontuarios.size());
    }

    @Test
    public void testProcesamiento_DifferentesTiposMotivo() {
        // Test con diferentes tipos de motivo
        String[] tiposMotivo = {"trivial", "moderada", "compleja"};

        for (int i = 0; i < tiposMotivo.length; i++) {
            ExcusaRequestDTO request = new ExcusaRequestDTO(
                "Empleado" + i,
                "empleado" + i + "@empresa.com",
                20000 + i,
                tiposMotivo[i],
                "Descripción de prueba para motivo " + tiposMotivo[i]
            );

            ExcusaResponseDTO excusa = excusaService.crearExcusa(request);

            assertNotNull(excusa);
            assertTrue(excusa.getTipoMotivo().toLowerCase()
                .contains(tiposMotivo[i].toLowerCase()));
        }

        // Verificar que todas fueron procesadas
        List<ExcusaResponseDTO> todasExcusas = excusaService.obtenerTodasExcusas();
        assertTrue(todasExcusas.size() >= 3);
    }
}