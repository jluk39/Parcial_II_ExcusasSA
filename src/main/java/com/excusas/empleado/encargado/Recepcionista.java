package com.excusas.empleado.encargado;

import com.excusas.excusa.IExcusa;
import com.excusas.excusa.motivo.Trivial;
import com.excusas.estrategia.IModoResolucion;
import com.excusas.servicio.IEmailSender;

public class Recepcionista extends EncargadoBase {

    public Recepcionista(String nombre, String email, int legajo,
                         IModoResolucion modo, IEmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
    }

    @Override
    public boolean puedeManejar(IExcusa excusa) {
        boolean puede = excusa.getMotivo().esTrivial();
        System.out.println("Recepcionista ¿puede manejar? " + puede);
        return puede;
    }

    @Override
    public void procesar(IExcusa excusa) {
        excusa.getMotivo().procesarPor(this, excusa);
    }

    public void procesar(Trivial motivo, IExcusa excusa) {
        getEmailSender().enviarEmail(
                excusa.getEmpleado().getEmail(),
                this.getEmail(),
                "Excusa trivial",
                "Motivo aceptado por recepción: " + motivo.getClass().getSimpleName()
        );
        System.out.println("Recepcionista procesó la excusa: " + excusa.getMotivo().getClass().getSimpleName());
    }
}
