package com.excusas.repository;

import com.excusas.model.empleado.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /**
     * Busca un empleado por su legajo
     */
    Optional<Empleado> findByLegajo(Integer legajo);

    /**
     * Busca un empleado por su email
     */
    Optional<Empleado> findByEmail(String email);

    /**
     * Busca un empleado por su nombre (ignorando mayúsculas/minúsculas)
     */
    Optional<Empleado> findByNombreIgnoreCase(String nombre);

    /**
     * Verifica si existe un empleado con el legajo dado
     */
    boolean existsByLegajo(Integer legajo);

    /**
     * Verifica si existe un empleado con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca empleados por nombre que contenga el texto dado
     */
    @Query("SELECT e FROM Empleado e WHERE UPPER(e.nombre) LIKE UPPER(CONCAT('%', :nombre, '%'))")
    List<Empleado> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Obtiene todos los empleados ordenados por nombre
     */
    List<Empleado> findAllByOrderByNombreAsc();
}
