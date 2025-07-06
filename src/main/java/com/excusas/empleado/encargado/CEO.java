package com.excusas.empleado.encargado;

import com.excusas.excusa.IExcusa;
import com.excusas.excusa.motivo.Inverosimil;
import com.excusas.estrategia.IModoResolucion;
import com.excusas.observer.IObserver;
import com.excusas.observer.AdministradorProntuarios;
import com.excusas.servicio.IEmailSender;
import com.excusas.excusa.Prontuario;

public class CEO extends EncargadoBase implements IObserver {

    public CEO(String nombre, String email, int legajo,
               IModoResolucion modo, IEmailSender emailSender) {
        super(nombre, email, legajo, modo, emailSender);
        AdministradorProntuarios.getInstancia().agregarObserver(this);
    }

    @Override
    public boolean puedeManejar(IExcusa excusa) {
        return excusa.getMotivo().esInverosimil();
    }

    @Override
    public void procesar(IExcusa excusa) {
        excusa.getMotivo().procesarPor(this, excusa);
    }

    public void procesar(Inverosimil motivo, IExcusa excusa) {
        getEmailSender().enviarEmail(
                excusa.getEmpleado().getEmail(),
                this.getEmail(),
                "Excusa inverosímil",
                "Aprobado por creatividad"
        );

        Prontuario prontuario = new Prontuario(excusa);
        AdministradorProntuarios.getInstancia().guardarProntuario(prontuario);
    }

    @Override
    public void actualizar(Prontuario prontuario) {
        System.out.println("CEO recibió una nueva excusa registrada:");
        System.out.println(prontuario);
    }
}
