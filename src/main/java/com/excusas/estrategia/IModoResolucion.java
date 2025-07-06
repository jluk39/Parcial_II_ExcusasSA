package com.excusas.estrategia;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.EncargadoBase;

public interface IModoResolucion {
    void resolver(EncargadoBase encargado, IExcusa excusa);
}
