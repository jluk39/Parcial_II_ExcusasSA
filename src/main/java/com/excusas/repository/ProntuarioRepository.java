package com.excusas.repository;

import com.excusas.model.excusa.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    /**
     * Busca prontuarios por legajo del empleado
     */
    List<Prontuario> findByLegajo(Integer legajo);

    /**
     * Busca prontuarios por nombre del empleado
     */
    List<Prontuario> findByNombreEmpleado(String nombreEmpleado);

    /**
     * Busca prontuarios por tipo de motivo
     */
    List<Prontuario> findByMotivo(String motivo);

    /**
     * Busca prontuarios por rango de fechas
     */
    @Query("SELECT p FROM Prontuario p WHERE p.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<Prontuario> findByFechaCreacionBetween(@Param("fechaDesde") LocalDateTime fechaDesde,
                                               @Param("fechaHasta") LocalDateTime fechaHasta);

    /**
     * Busca prontuarios por legajo del empleado y rango de fechas
     */
    @Query("SELECT p FROM Prontuario p WHERE p.legajo = :legajo AND p.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<Prontuario> findByLegajoAndFechaCreacionBetween(@Param("legajo") Integer legajo,
                                                        @Param("fechaDesde") LocalDateTime fechaDesde,
                                                        @Param("fechaHasta") LocalDateTime fechaHasta);

    /**
     * Busca prontuario por ID de excusa
     */
    @Query("SELECT p FROM Prontuario p WHERE p.excusa.id = :excusaId")
    Optional<Prontuario> findByExcusaId(@Param("excusaId") Long excusaId);

    /**
     * Obtiene todos los prontuarios ordenados por fecha de creación (más recientes primero)
     */
    List<Prontuario> findAllByOrderByFechaCreacionDesc();

    /**
     * Cuenta prontuarios por legajo
     */
    long countByLegajo(Integer legajo);

    /**
     * Obtiene los prontuarios más recientes (últimos N)
     */
    @Query("SELECT p FROM Prontuario p ORDER BY p.fechaCreacion DESC")
    List<Prontuario> findTopNByOrderByFechaCreacionDesc(@Param("limit") int limit);
}
