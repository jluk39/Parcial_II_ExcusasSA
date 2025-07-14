package com.excusas.repository;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.Excusa;
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
public class ExcusaRepositoryTest {

    @Autowired
    private ExcusaRepository excusaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado1;
    private Empleado empleado2;

    @BeforeEach
    public void setUp() {
        empleado1 = empleadoRepository.save(new Empleado("Juan Perez", "juan@excusas.com", 12345));
        empleado2 = empleadoRepository.save(new Empleado("Maria Garcia", "maria@excusas.com", 67890));
    }

    @Test
    public void testSaveAndFindExcusa() {
        // Given
        Excusa excusa = new Excusa();
        excusa.setTipoMotivo("Trivial");
        excusa.setDescripcion("Llegué tarde por el tráfico");
        excusa.setEmpleado(empleado1);
        excusa.setEstado("PENDIENTE");
        excusa.setFechaCreacion(LocalDateTime.now());

        // When
        Excusa savedExcusa = excusaRepository.save(excusa);

        // Then
        assertThat(savedExcusa.getId()).isNotNull();
        assertThat(savedExcusa.getTipoMotivo()).isEqualTo("Trivial");
        assertThat(savedExcusa.getDescripcion()).isEqualTo("Llegué tarde por el tráfico");
        assertThat(savedExcusa.getEstado()).isEqualTo("PENDIENTE");
        assertThat(savedExcusa.getEmpleado().getId()).isEqualTo(empleado1.getId());
    }

    @Test
    public void testFindByEmpleadoLegajo() {
        // Given
        Excusa excusa1 = createExcusa("Moderada", "Corte de luz", empleado1);
        Excusa excusa2 = createExcusa("Trivial", "Perdí el colectivo", empleado1);
        Excusa excusa3 = createExcusa("Compleja", "Abducción alienígena", empleado2);

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);

        // When
        List<Excusa> found = excusaRepository.findByEmpleadoLegajo(12345);

        // Then
        assertThat(found).hasSize(2);
        assertThat(found.stream().map(Excusa::getDescripcion))
                .containsExactlyInAnyOrder("Corte de luz", "Perdí el colectivo");
    }

    @Test
    public void testFindByEmpleadoNombre() {
        // Given
        Excusa excusa = createExcusa("Trivial", "Me quedé dormido", empleado1);
        excusaRepository.save(excusa);

        // When
        List<Excusa> found = excusaRepository.findByEmpleadoNombre("Juan Perez");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getDescripcion()).isEqualTo("Me quedé dormido");
    }

    @Test
    public void testFindByEstado() {
        // Given
        Excusa excusa1 = createExcusa("Trivial", "Tráfico", empleado1);
        excusa1.setEstado("PROCESADA");

        Excusa excusa2 = createExcusa("Moderada", "Familiar enfermo", empleado2);
        excusa2.setEstado("RECHAZADA");

        Excusa excusa3 = createExcusa("Compleja", "Ovnis", empleado1);
        excusa3.setEstado("PROCESADA");

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);

        // When
        List<Excusa> procesadas = excusaRepository.findByEstado("PROCESADA");
        List<Excusa> rechazadas = excusaRepository.findByEstado("RECHAZADA");

        // Then
        assertThat(procesadas).hasSize(2);
        assertThat(rechazadas).hasSize(1);
        assertThat(rechazadas.get(0).getDescripcion()).isEqualTo("Familiar enfermo");
    }

    @Test
    public void testFindByTipoMotivo() {
        // Given
        Excusa excusa1 = createExcusa("Trivial", "Despertador", empleado1);
        Excusa excusa2 = createExcusa("Trivial", "Transporte", empleado2);
        Excusa excusa3 = createExcusa("Moderada", "Corte luz", empleado1);

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);

        // When
        List<Excusa> triviales = excusaRepository.findByTipoMotivo("Trivial");

        // Then
        assertThat(triviales).hasSize(2);
        assertThat(triviales.stream().map(Excusa::getDescripcion))
                .containsExactlyInAnyOrder("Despertador", "Transporte");
    }

    @Test
    public void testFindByFechaCreacionBetween() {
        // Given
        LocalDateTime ayer = LocalDateTime.now().minusDays(1);
        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime manana = LocalDateTime.now().plusDays(1);

        Excusa excusa1 = createExcusa("Trivial", "Ayer", empleado1);
        excusa1.setFechaCreacion(ayer);

        Excusa excusa2 = createExcusa("Moderada", "Hoy", empleado2);
        excusa2.setFechaCreacion(hoy);

        Excusa excusa3 = createExcusa("Compleja", "Mañana", empleado1);
        excusa3.setFechaCreacion(manana);

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);

        // When
        List<Excusa> encontradas = excusaRepository.findByFechaCreacionBetween(
                ayer.minusHours(1),
                hoy.plusHours(1)
        );

        // Then
        assertThat(encontradas).hasSize(2);
        assertThat(encontradas.stream().map(Excusa::getDescripcion))
                .containsExactlyInAnyOrder("Ayer", "Hoy");
    }

    @Test
    public void testCountByEstado() {
        // Given
        excusaRepository.save(createExcusaWithEstado("PENDIENTE", empleado1));
        excusaRepository.save(createExcusaWithEstado("PROCESADA", empleado2));
        excusaRepository.save(createExcusaWithEstado("PROCESADA", empleado1));
        excusaRepository.save(createExcusaWithEstado("RECHAZADA", empleado2));

        // When & Then
        assertThat(excusaRepository.countByEstado("PENDIENTE")).isEqualTo(1);
        assertThat(excusaRepository.countByEstado("PROCESADA")).isEqualTo(2);
        assertThat(excusaRepository.countByEstado("RECHAZADA")).isEqualTo(1);
    }

    @Test
    public void testFindAllByOrderByFechaCreacionDesc() {
        // Given
        LocalDateTime tiempo1 = LocalDateTime.now().minusHours(2);
        LocalDateTime tiempo2 = LocalDateTime.now().minusHours(1);
        LocalDateTime tiempo3 = LocalDateTime.now();

        Excusa excusa1 = createExcusa("Trivial", "Primera", empleado1);
        excusa1.setFechaCreacion(tiempo1);

        Excusa excusa2 = createExcusa("Moderada", "Segunda", empleado2);
        excusa2.setFechaCreacion(tiempo2);

        Excusa excusa3 = createExcusa("Compleja", "Tercera", empleado1);
        excusa3.setFechaCreacion(tiempo3);

        excusaRepository.save(excusa1);
        excusaRepository.save(excusa2);
        excusaRepository.save(excusa3);

        // When
        List<Excusa> ordenadas = excusaRepository.findAllByOrderByFechaCreacionDesc();

        // Then
        assertThat(ordenadas).hasSize(3);
        assertThat(ordenadas.get(0).getDescripcion()).isEqualTo("Tercera");
        assertThat(ordenadas.get(1).getDescripcion()).isEqualTo("Segunda");
        assertThat(ordenadas.get(2).getDescripcion()).isEqualTo("Primera");
    }

    @Test
    public void testPersistenceIntegrity() {
        // Given
        Excusa excusa = createExcusa("Trivial", "Test persistencia", empleado1);
        excusa.setEncargadoProcesador("TestProcessor");

        // When
        Excusa saved = excusaRepository.save(excusa);
        excusaRepository.flush();

        // Then - verificar que los datos persisten correctamente
        Optional<Excusa> reloaded = excusaRepository.findById(saved.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getDescripcion()).isEqualTo("Test persistencia");
        assertThat(reloaded.get().getEncargadoProcesador()).isEqualTo("TestProcessor");
        assertThat(reloaded.get().getEmpleado().getNombre()).isEqualTo("Juan Perez");
    }

    private Excusa createExcusa(String tipoMotivo, String descripcion, Empleado empleado) {
        Excusa excusa = new Excusa();
        excusa.setTipoMotivo(tipoMotivo);
        excusa.setDescripcion(descripcion);
        excusa.setEmpleado(empleado);
        excusa.setEstado("PENDIENTE");
        excusa.setFechaCreacion(LocalDateTime.now());
        return excusa;
    }

    private Excusa createExcusaWithEstado(String estado, Empleado empleado) {
        Excusa excusa = createExcusa("Trivial", "Test " + estado, empleado);
        excusa.setEstado(estado);
        return excusa;
    }
}
