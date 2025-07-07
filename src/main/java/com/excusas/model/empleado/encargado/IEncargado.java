package com.excusas.model.empleado.encargado;

import com.excusas.model.excusa.IExcusa;

public interface IEncargado {
    void manejarExcusa(IExcusa excusa);
    boolean puedeManejar(IExcusa excusa);
    void procesar(IExcusa excusa);
}
