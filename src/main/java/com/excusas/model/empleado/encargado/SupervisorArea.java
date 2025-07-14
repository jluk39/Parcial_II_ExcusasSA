package com.excusas.model.empleado.encargado;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.CorteLuz;
import com.excusas.model.excusa.motivo.CuidadoFamiliar;
import com.excusas.model.estrategia.IModoResolucion;
import com.excusas.service.IEmailSender;

public class SupervisorArea extends EncargadoBase {

    public SupervisorArea(String nombre, String email, int legajo,
                          IModoResolucion modo, IEmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
    }

    @Override
    public boolean puedeManejar(IExcusa excusa) {
        return excusa.getMotivo().esModerada();
    }

    @Override
    public void procesar(IExcusa excusa) {
        excusa.getMotivo().procesarPor(this, excusa);
    }

    public void procesar(CorteLuz motivo, IExcusa excusa) {
        getEmailSender().enviarEmail(
                "EDESUR@mailfake.com.ar",
                this.getEmail(),
                "Consulta de corte",
                "¿Es verdad que se cortó la luz en todo el barrio?"
        );
    }

    public void procesar(CuidadoFamiliar motivo, IExcusa excusa) {
        getEmailSender().enviarEmail(
                excusa.getEmpleado().getEmail(),
                this.getEmail(),
                "Consulta de salud",
                "¿Está todo bien?"
        );
    }
}
