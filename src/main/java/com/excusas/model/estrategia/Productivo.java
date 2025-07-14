package com.excusas.model.estrategia;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.empleado.encargado.EncargadoBase;

public class Productivo implements IModoResolucion {
    @Override
    public void resolver(EncargadoBase encargado, IExcusa excusa) {
        if (encargado.puedeManejar(excusa)) {
            encargado.procesar(excusa);
        } else {
            System.out.println(encargado.getClass().getSimpleName() + " no puede resolver esto, delegando.");
            // Enviar email al CTO según la consigna
            encargado.getEmailSender().enviarEmail(
                "cto@excusas.com",
                encargado.getEmail(),
                "Excusa no procesada",
                "No pude procesar la excusa, derivando al siguiente encargado"
            );

            if (encargado.getSiguiente() != null) {
                encargado.getSiguiente().manejarExcusa(excusa);
            } else {
                System.out.println("Excusa rechazada: ningún encargado puede procesarla");
                excusa.setEstado("RECHAZADA");
            }
        }
    }
}
