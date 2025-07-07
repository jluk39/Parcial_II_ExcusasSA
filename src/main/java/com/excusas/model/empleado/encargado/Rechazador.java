package com.excusas.model.empleado.encargado;

import com.excusas.model.excusa.IExcusa;
import com.excusas.service.IEmailSender;
import com.excusas.excepciones.ExcusaNoManejadaException;

public class Rechazador implements IEncargado {

    private final IEmailSender emailSender;

    public Rechazador(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void manejarExcusa(IExcusa excusa) {
        procesar(excusa);
    }

    @Override
    public boolean puedeManejar(IExcusa excusa) {
        return true;
    }

    @Override
    public void procesar(IExcusa excusa) {
        emailSender.enviarEmail(
                excusa.getEmpleado().getEmail(),
                "rechazador@empresa.com",
                "Motivo demora",
                "Excusa rechazada: necesitamos pruebas contundentes"
        );
        throw new ExcusaNoManejadaException("Excusa rechazada: " + excusa.getMotivo().getClass().getSimpleName());
    }

}
