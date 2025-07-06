package com.excusas.excusa;

import com.excusas.empleado.Empleado;
import com.excusas.excusa.motivo.IMotivoExcusa;

public class Excusa implements IExcusa {

    private final IMotivoExcusa motivo;
    private final Empleado empleado;

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
}
