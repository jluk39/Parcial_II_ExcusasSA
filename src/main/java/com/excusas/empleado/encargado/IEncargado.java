package com.excusas.empleado.encargado;

import com.excusas.excusa.IExcusa;

public interface IEncargado {
    void manejarExcusa(IExcusa excusa);
    boolean puedeManejar(IExcusa excusa);
    void procesar(IExcusa excusa);
}
