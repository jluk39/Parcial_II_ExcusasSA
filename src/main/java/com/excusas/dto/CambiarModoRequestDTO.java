// src/main/java/com/excusas/dto/CambiarModoRequestDTO.java
package com.excusas.dto;

public class CambiarModoRequestDTO {
    private String encargadoId;
    private String nuevoModo;

    // Constructors
    public CambiarModoRequestDTO() {}

    public CambiarModoRequestDTO(String encargadoId, String nuevoModo) {
        this.encargadoId = encargadoId;
        this.nuevoModo = nuevoModo;
    }

    // Getters y Setters
    public String getEncargadoId() { return encargadoId; }
    public void setEncargadoId(String encargadoId) { this.encargadoId = encargadoId; }

    public String getNuevoModo() { return nuevoModo; }
    public void setNuevoModo(String nuevoModo) { this.nuevoModo = nuevoModo; }
}