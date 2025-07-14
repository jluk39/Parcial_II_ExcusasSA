// src/main/java/com/excusas/dto/EncargadoStatusDTO.java
package com.excusas.dto;

public class EncargadoStatusDTO {
    private String nombre;
    private String cargo;
    private String modoActual;
    private boolean activo;

    // Constructors
    public EncargadoStatusDTO() {}

    public EncargadoStatusDTO(String nombre, String cargo, String modoActual, boolean activo) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.modoActual = modoActual;
        this.activo = activo;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getModoActual() { return modoActual; }
    public void setModoActual(String modoActual) { this.modoActual = modoActual; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}