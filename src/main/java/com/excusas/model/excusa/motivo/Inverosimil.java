package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.*;
import com.excusas.model.excusa.IExcusa;

public class Inverosimil extends MotivoExcusa {

    @Override
    public void procesarPor(CEO c, IExcusa excusa) {
        c.procesar(this, excusa);
    }

    @Override
    public boolean esInverosimil() {
        return true;
    }

    @Override
    public String getTipoMotivo() {
        return "Inverosímil";
    }
}
