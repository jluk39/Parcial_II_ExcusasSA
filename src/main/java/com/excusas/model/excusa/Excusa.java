// src/main/java/com/excusas/model/excusa/Excusa.java
package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.motivo.IMotivoExcusa;

public class Excusa implements IExcusa {

    private Long id;
    private final IMotivoExcusa motivo;
    private final Empleado empleado;
    private String estado = "PENDIENTE";
    private String encargadoProcesador;

    public Excusa(IMotivoExcusa motivo, Empleado empleado) {
        this.motivo = motivo;
        this.empleado = empleado;
    }

    @Override
    public IMotivoExcusa getMotivo() {
        return motivo;
    }

    @Override
    public Empleado getEmpleado() {
        return empleado;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String getEncargadoProcesador() {
        return encargadoProcesador;
    }

    public void setEncargadoProcesador(String encargadoProcesador) {
        this.encargadoProcesador = encargadoProcesador;
    }
}