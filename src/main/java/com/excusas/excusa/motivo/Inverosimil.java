package com.excusas.excusa.motivo;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.*;

public class Inverosimil extends MotivoExcusa {

    @Override
    public void procesarPor(CEO c, IExcusa excusa) {
        c.procesar(this, excusa);
    }

    @Override
    public boolean esInverosimil() {
        return true;
    }
}
