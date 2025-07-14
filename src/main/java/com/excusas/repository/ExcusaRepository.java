// src/main/java/com/excusas/repository/ExcusaRepository.java
package com.excusas.repository;

import com.excusas.model.excusa.Excusa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExcusaRepository extends JpaRepository<Excusa, Long> {

    /**
     * Busca excusas por el legajo del empleado
     */
    @Query("SELECT e FROM Excusa e WHERE e.empleado.legajo = :legajo")
    List<Excusa> findByEmpleadoLegajo(@Param("legajo") Integer legajo);

    /**
     * Busca excusas por el nombre del empleado
     */
    @Query("SELECT e FROM Excusa e WHERE e.empleado.nombre = :nombre")
    List<Excusa> findByEmpleadoNombre(@Param("nombre") String nombre);

    /**
     * Busca excusas por estado
     */
    List<Excusa> findByEstado(String estado);

    /**
     * Busca excusas rechazadas
     */
    List<Excusa> findByEstadoOrderByFechaCreacionDesc(String estado);

    /**
     * Busca excusas por tipo de motivo
     */
    List<Excusa> findByTipoMotivo(String tipoMotivo);

    /**
     * Busca excusas por encargado procesador
     */
    List<Excusa> findByEncargadoProcesador(String encargadoProcesador);

    /**
     * Busca excusas por rango de fechas
     */
    @Query("SELECT e FROM Excusa e WHERE e.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<Excusa> findByFechaCreacionBetween(@Param("fechaDesde") LocalDateTime fechaDesde,
                                           @Param("fechaHasta") LocalDateTime fechaHasta);

    /**
     * Busca excusas por legajo del empleado y rango de fechas
     */
    @Query("SELECT e FROM Excusa e WHERE e.empleado.legajo = :legajo AND e.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<Excusa> findByEmpleadoLegajoAndFechaCreacionBetween(@Param("legajo") Integer legajo,
                                                            @Param("fechaDesde") LocalDateTime fechaDesde,
                                                            @Param("fechaHasta") LocalDateTime fechaHasta);

    /**
     * Busca excusas por múltiples criterios
     */
    @Query("SELECT e FROM Excusa e WHERE " +
           "(:legajo IS NULL OR e.empleado.legajo = :legajo) AND " +
           "(:tipoMotivo IS NULL OR e.tipoMotivo = :tipoMotivo) AND " +
           "(:encargado IS NULL OR e.encargadoProcesador = :encargado) AND " +
           "(:fechaDesde IS NULL OR e.fechaCreacion >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR e.fechaCreacion <= :fechaHasta)")
    List<Excusa> findByMultipleCriteria(@Param("legajo") Integer legajo,
                                       @Param("tipoMotivo") String tipoMotivo,
                                       @Param("encargado") String encargado,
                                       @Param("fechaDesde") LocalDateTime fechaDesde,
                                       @Param("fechaHasta") LocalDateTime fechaHasta);

    /**
     * Elimina excusas anteriores a una fecha límite
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Excusa e WHERE e.fechaCreacion < :fechaLimite")
    int deleteByFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);

    /**
     * Cuenta excusas por estado
     */
    long countByEstado(String estado);

    /**
     * Obtiene todas las excusas ordenadas por fecha de creación (más recientes primero)
     */
    List<Excusa> findAllByOrderByFechaCreacionDesc();
}