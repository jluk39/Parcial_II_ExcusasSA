package com.excusas.excusa.motivo;

import com.excusas.empleado.encargado.Recepcionista;
import com.excusas.excusa.IExcusa;

public class Trivial extends MotivoExcusa {
    @Override
    public void procesarPor(Recepcionista r, IExcusa excusa) {
        r.procesar(this, excusa);
    }

    @Override
    public boolean esTrivial() {
        return true;
    }
}

