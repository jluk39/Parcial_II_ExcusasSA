package com.excusas.excusa.motivo;

import com.excusas.empleado.encargado.*;
import com.excusas.excusa.IExcusa;

public abstract class MotivoExcusa implements IMotivoExcusa {

    @Override
    public void procesarPor(Recepcionista r, IExcusa excusa) {
    }

    @Override
    public void procesarPor(SupervisorArea s, IExcusa excusa) {
    }

    @Override
    public void procesarPor(GerenteRRHH g, IExcusa excusa) {
    }

    @Override
    public void procesarPor(CEO c, IExcusa excusa) {
    }

    @Override
    public boolean esTrivial() {
        return false;
    }

    @Override
    public boolean esModerada() {
        return false;
    }

    @Override
    public boolean esCompleja() {
        return false;
    }

    @Override
    public boolean esInverosimil() {
        return false;
    }
}
