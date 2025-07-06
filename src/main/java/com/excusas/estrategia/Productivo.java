package com.excusas.estrategia;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.EncargadoBase;

public class Productivo implements IModoResolucion {
    @Override
    public void resolver(EncargadoBase encargado, IExcusa excusa) {
        if (encargado.puedeManejar(excusa)) {
            encargado.procesar(excusa);
        } else {
            System.out.println(encargado.getClass().getSimpleName() + " no puede resolver esto, delegando.");  // para test

            if (encargado.getSiguiente() != null) {
                encargado.getSiguiente().manejarExcusa(excusa);
            }
        }
    }
}
