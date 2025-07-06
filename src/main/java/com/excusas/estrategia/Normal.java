package com.excusas.estrategia;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.EncargadoBase;

public class Normal implements IModoResolucion {
    @Override
    public void resolver(EncargadoBase encargado, IExcusa excusa) {
        encargado.manejarPorDefecto(excusa);
    }
}
