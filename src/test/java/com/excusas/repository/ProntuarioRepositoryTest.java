package com.excusas.repository;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.Prontuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
public class ProntuarioRepositoryTest {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ExcusaRepository excusaRepository;

    private Empleado empleado1;
    private Empleado empleado2;
    private Excusa excusa1;
    private Excusa excusa2;

    @BeforeEach
    public void setUp() {
        empleado1 = empleadoRepository.save(new Empleado("Juan Perez", "juan@excusas.com", 12345));
        empleado2 = empleadoRepository.save(new Empleado("Maria Garcia", "maria@excusas.com", 67890));

        excusa1 = createAndSaveExcusa("Inverosimil", "Abducción alienígena", empleado1);
        excusa2 = createAndSaveExcusa("Inverosimil", "Paloma robó mi bicicleta", empleado2);
    }

    @Test
    public void testSaveAndFindProntuario() {
        // Given
        Prontuario prontuario = new Prontuario();
        prontuario.setNombreEmpleado(empleado1.getNombre());
        prontuario.setLegajo(empleado1.getLegajo());
        prontuario.setMotivo("Inverosimil");
        prontuario.setExcusa(excusa1);
        prontuario.setObservacionesCeo("Aprobado por creatividad");
        prontuario.setFechaCreacion(LocalDateTime.now());

        // When
        Prontuario savedProntuario = prontuarioRepository.save(prontuario);

        // Then
        assertThat(savedProntuario.getId()).isNotNull();
        assertThat(savedProntuario.getNombreEmpleado()).isEqualTo("Juan Perez");
        assertThat(savedProntuario.getLegajo()).isEqualTo(12345);
        assertThat(savedProntuario.getMotivo()).isEqualTo("Inverosimil");
        assertThat(savedProntuario.getObservacionesCeo()).isEqualTo("Aprobado por creatividad");
        assertThat(savedProntuario.getExcusa().getId()).isEqualTo(excusa1.getId());
    }

    @Test
    public void testFindByLegajo() {
        // Given
        Prontuario prontuario1 = createProntuario(empleado1, excusa1, "Inverosimil");
        Prontuario prontuario2 = createProntuario(empleado1, excusa1, "Compleja");
        Prontuario prontuario3 = createProntuario(empleado2, excusa2, "Inverosimil");

        prontuarioRepository.save(prontuario1);
        prontuarioRepository.save(prontuario2);
        prontuarioRepository.save(prontuario3);

        // When
        List<Prontuario> found = prontuarioRepository.findByLegajo(12345);

        // Then
        assertThat(found).hasSize(2);
        assertThat(found.stream().map(Prontuario::getMotivo))
                .containsExactlyInAnyOrder("Inverosimil", "Compleja");
    }

    @Test
    public void testFindByNombreEmpleado() {
        // Given
        Prontuario prontuario = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuarioRepository.save(prontuario);

        // When
        List<Prontuario> found = prontuarioRepository.findByNombreEmpleado("Juan Perez");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getLegajo()).isEqualTo(12345);
    }

    @Test
    public void testFindByMotivo() {
        // Given
        Prontuario prontuario1 = createProntuario(empleado1, excusa1, "Inverosimil");
        Prontuario prontuario2 = createProntuario(empleado2, excusa2, "Inverosimil");
        Prontuario prontuario3 = createProntuario(empleado1, excusa1, "Compleja");

        prontuarioRepository.save(prontuario1);
        prontuarioRepository.save(prontuario2);
        prontuarioRepository.save(prontuario3);

        // When
        List<Prontuario> inverosimiles = prontuarioRepository.findByMotivo("Inverosimil");
        List<Prontuario> complejas = prontuarioRepository.findByMotivo("Compleja");

        // Then
        assertThat(inverosimiles).hasSize(2);
        assertThat(complejas).hasSize(1);
        assertThat(inverosimiles.stream().map(Prontuario::getNombreEmpleado))
                .containsExactlyInAnyOrder("Juan Perez", "Maria Garcia");
    }

    @Test
    public void testFindByFechaCreacionBetween() {
        // Given
        LocalDateTime ayer = LocalDateTime.now().minusDays(1);
        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime manana = LocalDateTime.now().plusDays(1);

        Prontuario prontuario1 = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuario1.setFechaCreacion(ayer);

        Prontuario prontuario2 = createProntuario(empleado2, excusa2, "Compleja");
        prontuario2.setFechaCreacion(hoy);

        Prontuario prontuario3 = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuario3.setFechaCreacion(manana);

        prontuarioRepository.save(prontuario1);
        prontuarioRepository.save(prontuario2);
        prontuarioRepository.save(prontuario3);

        // When
        List<Prontuario> encontrados = prontuarioRepository.findByFechaCreacionBetween(
                ayer.minusHours(1),
                hoy.plusHours(1)
        );

        // Then
        assertThat(encontrados).hasSize(2);
        assertThat(encontrados.stream().map(Prontuario::getMotivo))
                .containsExactlyInAnyOrder("Inverosimil", "Compleja");
    }

    @Test
    public void testFindByLegajoAndFechaCreacionBetween() {
        // Given
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);

        Prontuario prontuario1 = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuario1.setFechaCreacion(LocalDateTime.now());

        Prontuario prontuario2 = createProntuario(empleado2, excusa2, "Compleja");
        prontuario2.setFechaCreacion(LocalDateTime.now());

        Prontuario prontuario3 = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuario3.setFechaCreacion(LocalDateTime.now().minusDays(2)); // Fuera del rango

        prontuarioRepository.save(prontuario1);
        prontuarioRepository.save(prontuario2);
        prontuarioRepository.save(prontuario3);

        // When
        List<Prontuario> encontrados = prontuarioRepository.findByLegajoAndFechaCreacionBetween(
                12345, inicio, fin
        );

        // Then
        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getMotivo()).isEqualTo("Inverosimil");
    }

    @Test
    public void testFindByExcusaId() {
        // Given
        Prontuario prontuario = createProntuario(empleado1, excusa1, "Inverosimil");
        prontuarioRepository.save(prontuario);

        // When
        Optional<Prontuario> found = prontuarioRepository.findByExcusaId(excusa1.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombreEmpleado()).isEqualTo("Juan Perez");
        assertThat(found.get().getExcusa().getId()).isEqualTo(excusa1.getId());
    }

    @Test
    public void testFindAllByOrderByFechaCreacionDesc() {
        // Given
        LocalDateTime tiempo1 = LocalDateTime.now().minusHours(2);
        LocalDateTime tiempo2 = LocalDateTime.now().minusHours(1);
        LocalDateTime tiempo3 = LocalDateTime.now();

        Prontuario prontuario1 = createProntuario(empleado1, excusa1, "Primero");
        prontuario1.setFechaCreacion(tiempo1);

        Prontuario prontuario2 = createProntuario(empleado2, excusa2, "Segundo");
        prontuario2.setFechaCreacion(tiempo2);

        Prontuario prontuario3 = createProntuario(empleado1, excusa1, "Tercero");
        prontuario3.setFechaCreacion(tiempo3);

        prontuarioRepository.save(prontuario1);
        prontuarioRepository.save(prontuario2);
        prontuarioRepository.save(prontuario3);

        // When
        List<Prontuario> ordenados = prontuarioRepository.findAllByOrderByFechaCreacionDesc();

        // Then
        assertThat(ordenados).hasSize(3);
        assertThat(ordenados.get(0).getMotivo()).isEqualTo("Tercero");
        assertThat(ordenados.get(1).getMotivo()).isEqualTo("Segundo");
        assertThat(ordenados.get(2).getMotivo()).isEqualTo("Primero");
    }

    @Test
    public void testCountByLegajo() {
        // Given
        prontuarioRepository.save(createProntuario(empleado1, excusa1, "Inverosimil"));
        prontuarioRepository.save(createProntuario(empleado1, excusa1, "Compleja"));
        prontuarioRepository.save(createProntuario(empleado2, excusa2, "Inverosimil"));

        // When & Then
        assertThat(prontuarioRepository.countByLegajo(12345)).isEqualTo(2);
        assertThat(prontuarioRepository.countByLegajo(67890)).isEqualTo(1);
        assertThat(prontuarioRepository.countByLegajo(99999)).isEqualTo(0);
    }

    @Test
    public void testPersistenceIntegrity() {
        // Given
        Prontuario prontuario = createProntuario(empleado1, excusa1, "Test Persistence");
        prontuario.setObservacionesCeo("Test CEO observations");

        // When
        Prontuario saved = prontuarioRepository.save(prontuario);
        prontuarioRepository.flush();

        // Then - verificar que los datos persisten correctamente
        Optional<Prontuario> reloaded = prontuarioRepository.findById(saved.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getMotivo()).isEqualTo("Test Persistence");
        assertThat(reloaded.get().getObservacionesCeo()).isEqualTo("Test CEO observations");
        assertThat(reloaded.get().getExcusa().getId()).isEqualTo(excusa1.getId());
        assertThat(reloaded.get().getNombreEmpleado()).isEqualTo("Juan Perez");
    }

    private Excusa createAndSaveExcusa(String tipoMotivo, String descripcion, Empleado empleado) {
        Excusa excusa = new Excusa();
        excusa.setTipoMotivo(tipoMotivo);
        excusa.setDescripcion(descripcion);
        excusa.setEmpleado(empleado);
        excusa.setEstado("PROCESADA");
        excusa.setFechaCreacion(LocalDateTime.now());
        return excusaRepository.save(excusa);
    }

    private Prontuario createProntuario(Empleado empleado, Excusa excusa, String motivo) {
        Prontuario prontuario = new Prontuario();
        prontuario.setNombreEmpleado(empleado.getNombre());
        prontuario.setLegajo(empleado.getLegajo());
        prontuario.setMotivo(motivo);
        prontuario.setExcusa(excusa);
        prontuario.setObservacionesCeo("Aprobado por creatividad");
        prontuario.setFechaCreacion(LocalDateTime.now());
        return prontuario;
    }
}
