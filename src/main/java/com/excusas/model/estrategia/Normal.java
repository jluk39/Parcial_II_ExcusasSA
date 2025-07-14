package com.excusas.model.estrategia;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.empleado.encargado.EncargadoBase;

public class Normal implements IModoResolucion {
    @Override
    public void resolver(EncargadoBase encargado, IExcusa excusa) {
        encargado.manejarPorDefecto(excusa);
    }
}
