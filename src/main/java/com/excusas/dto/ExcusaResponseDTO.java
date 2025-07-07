// src/main/java/com/excusas/dto/ExcusaResponseDTO.java
package com.excusas.dto;

import java.time.LocalDateTime;

public class ExcusaResponseDTO {
    private Long id;
    private String empleadoNombre;
    private String tipoMotivo;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String encargadoProcesador;

    // Constructors
    public ExcusaResponseDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }

    public String getTipoMotivo() { return tipoMotivo; }
    public void setTipoMotivo(String tipoMotivo) { this.tipoMotivo = tipoMotivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEncargadoProcesador() { return encargadoProcesador; }
    public void setEncargadoProcesador(String encargadoProcesador) { this.encargadoProcesador = encargadoProcesador; }
}