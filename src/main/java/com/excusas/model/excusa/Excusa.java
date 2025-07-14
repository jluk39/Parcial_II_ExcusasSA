// src/main/java/com/excusas/model/excusa/Excusa.java
package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.motivo.IMotivoExcusa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "excusas")
public class Excusa implements IExcusa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El motivo no puede ser nulo")
    @Column(nullable = false)
    private String tipoMotivo; // Guardamos el tipo como string para JPA

    @Transient
    private IMotivoExcusa motivo; // Mantenemos la referencia para el comportamiento

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "encargado_procesador", nullable = false)
    private String encargadoProcesador = "Sistema";

    @Column(name = "descripcion", length = 500)
    private String descripcion = "";

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    // Constructor por defecto para JPA
    public Excusa() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Excusa(IMotivoExcusa motivo, Empleado empleado) {
        this.motivo = motivo;
        this.empleado = empleado;
        this.tipoMotivo = motivo.getClass().getSimpleName();
        this.fechaCreacion = LocalDateTime.now();
    }

    @Override
    public IMotivoExcusa getMotivo() { return motivo; }

    @Override
    public Empleado getEmpleado() { return empleado; }

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    @Override
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String getEncargadoProcesador() { return encargadoProcesador; }

    public void setEncargadoProcesador(String encargadoProcesador) {
        this.encargadoProcesador = encargadoProcesador;
    }

    @Override
    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método para marcar como procesada
    public void marcarComoProcesada(String procesador) {
        this.estado = "PROCESADA";
        this.encargadoProcesador = procesador;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    // Método para marcar como rechazada
    public void marcarComoRechazada() {
        this.estado = "RECHAZADA";
        this.encargadoProcesador = "Sistema";
        this.fechaProcesamiento = LocalDateTime.now();
    }

    // Getters y setters adicionales para JPA
    public String getTipoMotivo() {
        return tipoMotivo;
    }

    public void setTipoMotivo(String tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setMotivo(IMotivoExcusa motivo) {
        this.motivo = motivo;
        this.tipoMotivo = motivo.getClass().getSimpleName();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    @Override
    public String toString() {
        return "Excusa{" +
                "id=" + id +
                ", tipoMotivo='" + tipoMotivo + '\'' +
                ", empleado=" + (empleado != null ? empleado.getNombre() : "null") +
                ", estado='" + estado + '\'' +
                ", encargadoProcesador='" + encargadoProcesador + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}