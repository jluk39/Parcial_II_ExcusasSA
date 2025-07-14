package com.excusas.repository;

import com.excusas.model.empleado.Empleado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    public void testSaveAndFindEmpleado() {
        // Given
        Empleado empleado = new Empleado("Juan Perez", "juan@excusas.com", 12345);

        // When
        Empleado savedEmpleado = empleadoRepository.save(empleado);

        // Then
        assertThat(savedEmpleado.getId()).isNotNull();
        assertThat(savedEmpleado.getNombre()).isEqualTo("Juan Perez");
        assertThat(savedEmpleado.getEmail()).isEqualTo("juan@excusas.com");
        assertThat(savedEmpleado.getLegajo()).isEqualTo(12345);
    }

    @Test
    public void testFindByLegajo() {
        // Given
        Empleado empleado = new Empleado("Maria Garcia", "maria@excusas.com", 67890);
        empleadoRepository.save(empleado);

        // When
        Optional<Empleado> found = empleadoRepository.findByLegajo(67890);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Maria Garcia");
    }

    @Test
    public void testFindByEmail() {
        // Given
        Empleado empleado = new Empleado("Carlos Rodriguez", "carlos@excusas.com", 11111);
        empleadoRepository.save(empleado);

        // When
        Optional<Empleado> found = empleadoRepository.findByEmail("carlos@excusas.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getLegajo()).isEqualTo(11111);
    }

    @Test
    public void testExistsByLegajo() {
        // Given
        Empleado empleado = new Empleado("Ana Lopez", "ana@excusas.com", 22222);
        empleadoRepository.save(empleado);

        // When & Then
        assertThat(empleadoRepository.existsByLegajo(22222)).isTrue();
        assertThat(empleadoRepository.existsByLegajo(99999)).isFalse();
    }

    @Test
    public void testExistsByEmail() {
        // Given
        Empleado empleado = new Empleado("Pedro Martinez", "pedro@excusas.com", 33333);
        empleadoRepository.save(empleado);

        // When & Then
        assertThat(empleadoRepository.existsByEmail("pedro@excusas.com")).isTrue();
        assertThat(empleadoRepository.existsByEmail("noexiste@excusas.com")).isFalse();
    }

    @Test
    public void testFindByNombreContainingIgnoreCase() {
        // Given
        empleadoRepository.save(new Empleado("Juan Carlos", "juan1@excusas.com", 44444));
        empleadoRepository.save(new Empleado("Maria Juan", "maria1@excusas.com", 55555));
        empleadoRepository.save(new Empleado("Pedro Lopez", "pedro1@excusas.com", 66666));

        // When
        List<Empleado> found = empleadoRepository.findByNombreContainingIgnoreCase("juan");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found.stream().map(Empleado::getNombre))
                .containsExactlyInAnyOrder("Juan Carlos", "Maria Juan");
    }

    @Test
    public void testFindAllByOrderByNombreAsc() {
        // Given
        empleadoRepository.save(new Empleado("Zoe", "zoe@excusas.com", 77777));
        empleadoRepository.save(new Empleado("Ana", "ana2@excusas.com", 88888));
        empleadoRepository.save(new Empleado("Manuel", "manuel@excusas.com", 99999));

        // When
        List<Empleado> found = empleadoRepository.findAllByOrderByNombreAsc();

        // Then
        assertThat(found).hasSize(3);
        assertThat(found.get(0).getNombre()).isEqualTo("Ana");
        assertThat(found.get(1).getNombre()).isEqualTo("Manuel");
        assertThat(found.get(2).getNombre()).isEqualTo("Zoe");
    }

    @Test
    public void testPersistenceIntegrity() {
        // Given
        Empleado empleado = new Empleado("Test User", "test@excusas.com", 10101);

        // When
        Empleado saved = empleadoRepository.save(empleado);
        empleadoRepository.flush(); // Forzar escritura a DB

        // Then - verificar que los datos persisten correctamente
        Optional<Empleado> reloaded = empleadoRepository.findById(saved.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getNombre()).isEqualTo("Test User");
        assertThat(reloaded.get().getEmail()).isEqualTo("test@excusas.com");
        assertThat(reloaded.get().getLegajo()).isEqualTo(10101);
    }
}
