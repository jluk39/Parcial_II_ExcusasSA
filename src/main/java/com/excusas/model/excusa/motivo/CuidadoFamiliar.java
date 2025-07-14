package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.*;
import com.excusas.model.excusa.IExcusa;


public class CuidadoFamiliar extends Moderada {

    @Override
    public void procesarPor(SupervisorArea s, IExcusa excusa) {
        String cuerpo = "¿Está todo bien?";
        s.getEmailSender().enviarEmail(
                excusa.getEmpleado().getEmail(),
                s.getEmail(),
                "Consulta de salud",
                cuerpo
        );
    }
}
