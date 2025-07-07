package com.excusas.model.empleado.encargado;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.Compleja;
import com.excusas.model.estrategia.IModoResolucion;
import com.excusas.service.IEmailSender;

public class GerenteRRHH extends EncargadoBase {

    public GerenteRRHH(String nombre, String email, int legajo,
                       IModoResolucion modo, IEmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
    }

    @Override
    public boolean puedeManejar(IExcusa excusa) {
        return excusa.getMotivo().esCompleja();
    }

    @Override
    public void procesar(IExcusa excusa) {
        excusa.getMotivo().procesarPor(this, excusa);
    }

    public void procesar(Compleja motivo, IExcusa excusa) {
        getEmailSender().enviarEmail(
                excusa.getEmpleado().getEmail(),
                this.getEmail(),
                "Evaluación de RRHH",
                "Tu caso está siendo evaluado por el área de Recursos Humanos."
        );
    }
}
