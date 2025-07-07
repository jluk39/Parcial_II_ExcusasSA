package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.*;
import com.excusas.model.excusa.IExcusa;


public class CorteLuz extends Moderada {

    @Override
    public void procesarPor(SupervisorArea s, IExcusa excusa) {
        String cuerpo = "¿Es verdad que se cortó la luz en todo el barrio?";
        s.getEmailSender().enviarEmail("EDESUR@mailfake.com.ar", s.getEmail(), "Consulta de corte", cuerpo);
    }
}
