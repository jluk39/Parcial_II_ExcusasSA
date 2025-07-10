package com.excusas.model.empleado;

import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.IMotivoExcusa;

public class Empleado {
    private final String nombre;
    private final String email;
    private final Integer legajo;

    public Empleado(String nombre, String email, int legajo) {
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public IExcusa generarExcusa(IMotivoExcusa motivo) {
        return new Excusa(motivo, this);
    }
}
