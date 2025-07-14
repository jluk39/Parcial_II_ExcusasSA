package com.excusas.dto;

public class EmpleadoResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private Integer legajo;

    // Constructores
    public EmpleadoResponseDTO() {}

    public EmpleadoResponseDTO(Long id, String nombre, String email, Integer legajo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }
}
