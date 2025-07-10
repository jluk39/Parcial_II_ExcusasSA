// src/main/java/com/excusas/model/excusa/Excusa.java
package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.motivo.IMotivoExcusa;

public class Excusa implements IExcusa {
    private Long id;
    private final IMotivoExcusa motivo;
    private final Empleado empleado;
    private String estado = "PENDIENTE";
    private String encargadoProcesador = "Sistema";
    private String descripcion = "";

    public Excusa(IMotivoExcusa motivo, Empleado empleado) {
        this.motivo = motivo;
        this.empleado = empleado;
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
    }

    // Método para marcar como rechazada
    public void marcarComoRechazada() {
        this.estado = "RECHAZADA";
        this.encargadoProcesador = "Sistema";
    }
}