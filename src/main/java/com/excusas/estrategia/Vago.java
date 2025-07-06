package com.excusas.estrategia;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.EncargadoBase;

public class Vago implements IModoResolucion {
    @Override
    public void resolver(EncargadoBase encargado, IExcusa excusa) {
        System.out.println(encargado.getClass().getSimpleName() + " está ocupado y no hará nada.");
        if (encargado.getSiguiente() != null) {
            encargado.getSiguiente().manejarExcusa(excusa);
        }
    }
}
