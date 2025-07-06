package com.excusas.excusa.motivo;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.*;

public class CorteLuz extends Moderada {

    @Override
    public void procesarPor(SupervisorArea s, IExcusa excusa) {
        String cuerpo = "¿Es verdad que se cortó la luz en todo el barrio?";
        s.getEmailSender().enviarEmail("EDESUR@mailfake.com.ar", s.getEmail(), "Consulta de corte", cuerpo);
    }
}
