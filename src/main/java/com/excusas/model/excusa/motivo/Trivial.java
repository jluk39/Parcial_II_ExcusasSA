package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.Recepcionista;
import com.excusas.model.excusa.IExcusa;

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

