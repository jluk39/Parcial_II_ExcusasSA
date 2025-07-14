package com.excusas.model.estrategia;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.empleado.encargado.EncargadoBase;

public interface IModoResolucion {
    void resolver(EncargadoBase encargado, IExcusa excusa);
}
