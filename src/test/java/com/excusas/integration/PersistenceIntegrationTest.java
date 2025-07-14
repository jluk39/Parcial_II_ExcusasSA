package com.excusas.integration;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.Prontuario;
import com.excusas.repository.EmpleadoRepository;
import com.excusas.repository.ExcusaRepository;
import com.excusas.repository.ProntuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:integration_test_db"
})
@Transactional
public class PersistenceIntegrationTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ExcusaRepository excusaRepository;

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Test
    public void testCompleteDataPersistenceFlow() {
        // 1. CREAR Y PERSISTIR EMPLEADO
        Empleado empleado = new Empleado("Juan Carlos Pérez", "juan.carlos@excusas.com", 12345);
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // Verificar que se persistió correctamente
        assertThat(empleadoGuardado.getId()).isNotNull();
        assertThat(empleadoRepository.existsByLegajo(12345)).isTrue();
        assertThat(empleadoRepository.existsByEmail("juan.carlos@excusas.com")).isTrue();

        // 2. CREAR Y PERSISTIR EXCUSA
        Excusa excusa = new Excusa();
        excusa.setTipoMotivo("Inverosimil");
        excusa.setDescripcion("Fui abducido por extraterrestres");
        excusa.setEmpleado(empleadoGuardado);
        excusa.setEstado("PROCESADA");
        excusa.setEncargadoProcesador("CEO");
        excusa.setFechaCreacion(LocalDateTime.now());

        Excusa excusaGuardada = excusaRepository.save(excusa);

        // Verificar que se persistió correctamente
        assertThat(excusaGuardada.getId()).isNotNull();
        assertThat(excusaRepository.countByEstado("PROCESADA")).isEqualTo(1);

        // 3. CREAR Y PERSISTIR PRONTUARIO
        Prontuario prontuario = new Prontuario();
        prontuario.setNombreEmpleado(empleadoGuardado.getNombre());
        prontuario.setLegajo(empleadoGuardado.getLegajo());
        prontuario.setMotivo("Inverosimil");
        prontuario.setExcusa(excusaGuardada);
        prontuario.setObservacionesCeo("Aprobado por creatividad excepcional");
        prontuario.setFechaCreacion(LocalDateTime.now());

        Prontuario prontuarioGuardado = prontuarioRepository.save(prontuario);

        // Verificar que se persistió correctamente
        assertThat(prontuarioGuardado.getId()).isNotNull();
        assertThat(prontuarioRepository.countByLegajo(12345)).isEqualTo(1);

        // 4. VERIFICAR RECUPERACIÓN DE DATOS
        // Buscar empleado por legajo
        Optional<Empleado> empleadoEncontrado = empleadoRepository.findByLegajo(12345);
        assertThat(empleadoEncontrado).isPresent();
        assertThat(empleadoEncontrado.get().getNombre()).isEqualTo("Juan Carlos Pérez");

        // Buscar excusas del empleado
        List<Excusa> excusasEmpleado = excusaRepository.findByEmpleadoLegajo(12345);
        assertThat(excusasEmpleado).hasSize(1);
        assertThat(excusasEmpleado.get(0).getDescripcion()).isEqualTo("Fui abducido por extraterrestres");

        // Buscar prontuarios del empleado
        List<Prontuario> prontuariosEmpleado = prontuarioRepository.findByLegajo(12345);
        assertThat(prontuariosEmpleado).hasSize(1);
        assertThat(prontuariosEmpleado.get(0).getObservacionesCeo()).isEqualTo("Aprobado por creatividad excepcional");

        // 5. VERIFICAR RELACIONES JPA
        // Verificar que la relación entre excusa y empleado funciona
        Excusa excusaRecuperada = excusaRepository.findById(excusaGuardada.getId()).get();
        assertThat(excusaRecuperada.getEmpleado().getNombre()).isEqualTo("Juan Carlos Pérez");

        // Verificar que la relación entre prontuario y excusa funciona
        Prontuario prontuarioRecuperado = prontuarioRepository.findById(prontuarioGuardado.getId()).get();
        assertThat(prontuarioRecuperado.getExcusa().getId()).isEqualTo(excusaGuardada.getId());
        assertThat(prontuarioRecuperado.getExcusa().getDescripcion()).isEqualTo("Fui abducido por extraterrestres");

        System.out.println("✅ PERSISTENCIA VERIFICADA: Los datos se guardaron y recuperaron correctamente de la base de datos H2");
    }

    @Test
    public void testMultipleEmployeesAndExcusesFlow() {
        // Crear múltiples empleados
        Empleado empleado1 = empleadoRepository.save(new Empleado("María García", "maria@excusas.com", 11111));
        Empleado empleado2 = empleadoRepository.save(new Empleado("Pedro López", "pedro@excusas.com", 22222));
        Empleado empleado3 = empleadoRepository.save(new Empleado("Ana Martínez", "ana@excusas.com", 33333));

        // Crear múltiples excusas
        Excusa excusa1 = createExcusa("Trivial", "Llegué tarde por tráfico", empleado1, "PROCESADA");
        Excusa excusa2 = createExcusa("Moderada", "Corte de luz en el barrio", empleado2, "PROCESADA");
        Excusa excusa3 = createExcusa("Inverosimil", "Ovnis en mi casa", empleado3, "PROCESADA");
        Excusa excusa4 = createExcusa("Trivial", "Perdí el colectivo", empleado1, "RECHAZADA");

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);
        excusaRepository.save(excusa4);

        // Crear prontuario solo para la excusa inverosímil
        Prontuario prontuario = new Prontuario();
        prontuario.setNombreEmpleado(empleado3.getNombre());
        prontuario.setLegajo(empleado3.getLegajo());
        prontuario.setMotivo("Inverosimil");
        prontuario.setExcusa(excusa3);
        prontuario.setObservacionesCeo("Aprobado por creatividad");
        prontuario.setFechaCreacion(LocalDateTime.now());

        prontuarioRepository.save(prontuario);

        // VERIFICACIONES
        // Verificar que todos los empleados se persistieron
        assertThat(empleadoRepository.count()).isEqualTo(3);
        assertThat(empleadoRepository.existsByLegajo(11111)).isTrue();
        assertThat(empleadoRepository.existsByLegajo(22222)).isTrue();
        assertThat(empleadoRepository.existsByLegajo(33333)).isTrue();

        // Verificar que todas las excusas se persistieron
        assertThat(excusaRepository.count()).isEqualTo(4);
        assertThat(excusaRepository.countByEstado("PROCESADA")).isEqualTo(3);
        assertThat(excusaRepository.countByEstado("RECHAZADA")).isEqualTo(1);

        // Verificar que solo hay un prontuario
        assertThat(prontuarioRepository.count()).isEqualTo(1);

        // Verificar búsquedas específicas
        List<Excusa> excusasMaria = excusaRepository.findByEmpleadoLegajo(11111);
        assertThat(excusasMaria).hasSize(2);

        List<Excusa> excusasTriviales = excusaRepository.findByTipoMotivo("Trivial");
        assertThat(excusasTriviales).hasSize(2);

        List<Prontuario> prontuariosAna = prontuarioRepository.findByLegajo(33333);
        assertThat(prontuariosAna).hasSize(1);
        assertThat(prontuariosAna.get(0).getMotivo()).isEqualTo("Inverosimil");

        System.out.println("✅ PERSISTENCIA MÚLTIPLE VERIFICADA: Múltiples entidades se persistieron correctamente");
    }

    @Test
    public void testDatabaseConsistencyAfterOperations() {
        // Crear datos iniciales
        Empleado empleado = empleadoRepository.save(new Empleado("Test User", "test@excusas.com", 99999));

        Excusa excusa1 = createExcusa("Trivial", "Test 1", empleado, "PENDIENTE");
        Excusa excusa2 = createExcusa("Moderada", "Test 2", empleado, "PROCESADA");

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);

        // Verificar estado inicial
        assertThat(excusaRepository.countByEstado("PENDIENTE")).isEqualTo(1);
        assertThat(excusaRepository.countByEstado("PROCESADA")).isEqualTo(1);

        // Modificar estado de una excusa
        excusa1.setEstado("PROCESADA");
        excusa1.setEncargadoProcesador("Recepcionista");
        excusaRepository.save(excusa1);

        // Verificar que el cambio se persistió
        assertThat(excusaRepository.countByEstado("PENDIENTE")).isEqualTo(0);
        assertThat(excusaRepository.countByEstado("PROCESADA")).isEqualTo(2);

        // Verificar que la excusa modificada mantiene su integridad
        Excusa excusaModificada = excusaRepository.findById(excusa1.getId()).get();
        assertThat(excusaModificada.getEstado()).isEqualTo("PROCESADA");
        assertThat(excusaModificada.getEncargadoProcesador()).isEqualTo("Recepcionista");
        assertThat(excusaModificada.getDescripcion()).isEqualTo("Test 1");

        System.out.println("✅ CONSISTENCIA DE BASE DE DATOS VERIFICADA: Las modificaciones se persisten correctamente");
    }

    private Excusa createExcusa(String tipoMotivo, String descripcion, Empleado empleado, String estado) {
        Excusa excusa = new Excusa();
        excusa.setTipoMotivo(tipoMotivo);
        excusa.setDescripcion(descripcion);
        excusa.setEmpleado(empleado);
        excusa.setEstado(estado);
        excusa.setFechaCreacion(LocalDateTime.now());
        return excusa;
    }
}
