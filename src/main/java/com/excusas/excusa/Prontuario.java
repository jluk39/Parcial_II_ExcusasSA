package com.excusas.excusa;

import com.excusas.empleado.Empleado;

public class Prontuario {

    private final String nombreEmpleado;
    private final int legajo;
    private final String motivo;

    public Prontuario(IExcusa excusa) {
        Empleado e = excusa.getEmpleado();
        this.nombreEmpleado = e.getNombre();
        this.legajo = e.getLegajo();
        this.motivo = excusa.getMotivo().getClass().getSimpleName();
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "nombreEmpleado='" + nombreEmpleado + '\'' +
                ", legajo=" + legajo +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
