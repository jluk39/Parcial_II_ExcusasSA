package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.*;
import com.excusas.model.excusa.IExcusa;

public class Trivial extends MotivoExcusa {

    @Override
    public boolean esTrivial() {
        return true;
    }

    @Override
    public String getTipoMotivo() {
        return "Trivial";
    }

    @Override
    public void procesarPor(Recepcionista r, IExcusa excusa) {
        r.procesar(this, excusa);
    }
}

