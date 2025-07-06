package com.excusas.excusa.motivo;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.*;

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
