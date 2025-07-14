package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "prontuarios")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del empleado no puede ser nulo")
    @Column(name = "nombre_empleado", nullable = false)
    private String nombreEmpleado;

    @NotNull(message = "El legajo no puede ser nulo")
    @Column(nullable = false)
    private Integer legajo;

    @NotNull(message = "El motivo no puede ser nulo")
    @Column(nullable = false)
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excusa_id", nullable = false)
    private Excusa excusa;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "observaciones_ceo", length = 1000)
    private String observacionesCeo = "Aprobado por creatividad";

    // Constructor por defecto para JPA
    public Prontuario() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Prontuario(IExcusa excusa) {
        Empleado e = excusa.getEmpleado();
        this.nombreEmpleado = e.getNombre();
        this.legajo = e.getLegajo();
        this.motivo = excusa.getMotivo().getClass().getSimpleName();
        this.excusa = (Excusa) excusa; // Cast para JPA
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters originales
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getMotivo() {
        return motivo;
    }

    // Getters y setters adicionales para JPA
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Excusa getExcusa() {
        return excusa;
    }

    public void setExcusa(Excusa excusa) {
        this.excusa = excusa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getObservacionesCeo() {
        return observacionesCeo;
    }

    public void setObservacionesCeo(String observacionesCeo) {
        this.observacionesCeo = observacionesCeo;
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "id=" + id +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", legajo=" + legajo +
                ", motivo='" + motivo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
