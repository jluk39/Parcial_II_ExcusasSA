// src/main/java/com/excusas/dto/ExcusaRequestDTO.java
package com.excusas.dto;

public class ExcusaRequestDTO {
    private String empleadoNombre;
    private String empleadoEmail;
    private Integer empleadoLegajo;
    private String tipoMotivo;
    private String descripcion;

    // Constructors
    public ExcusaRequestDTO() {}

    public ExcusaRequestDTO(String empleadoNombre, String empleadoEmail, Integer empleadoLegajo, String tipoMotivo, String descripcion) {
        this.empleadoNombre = empleadoNombre;
        this.empleadoEmail = empleadoEmail;
        this.empleadoLegajo = empleadoLegajo;
        this.tipoMotivo = tipoMotivo;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }

    public String getEmpleadoEmail() { return empleadoEmail; }
    public void setEmpleadoEmail(String empleadoEmail) { this.empleadoEmail = empleadoEmail; }

    public Integer getEmpleadoLegajo() { return empleadoLegajo; }
    public void setEmpleadoLegajo(Integer empleadoLegajo) { this.empleadoLegajo = empleadoLegajo; }

    public String getTipoMotivo() { return tipoMotivo; }
    public void setTipoMotivo(String tipoMotivo) { this.tipoMotivo = tipoMotivo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}