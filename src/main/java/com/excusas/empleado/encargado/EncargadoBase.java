package com.excusas.empleado.encargado;

import com.excusas.empleado.Empleado;
import com.excusas.estrategia.IModoResolucion;
import com.excusas.excusa.IExcusa;
import com.excusas.servicio.IEmailSender;
import com.excusas.excepciones.ExcusaNoManejadaException;

public abstract class EncargadoBase extends Empleado implements IEncargado {

    private final IModoResolucion modo;
    private final IEmailSender emailSender;
    private IEncargado siguiente;

    public EncargadoBase(String nombre, String email, int legajo,
                         IModoResolucion modo, IEmailSender emailSender) {
        super(nombre, email, legajo);
        this.modo = modo;
        this.emailSender = emailSender;
    }

    public void setSiguiente(IEncargado siguiente) {
        this.siguiente = siguiente;
    }

    public IEncargado getSiguiente() {
        return siguiente;
    }

    public IEmailSender getEmailSender() {
        return emailSender;
    }

    @Override
    public void manejarExcusa(IExcusa excusa) {
        if (modo != null) {
            modo.resolver(this, excusa);
        } else {
            manejarPorDefecto(excusa);
        }
    }

    public void manejarPorDefecto(IExcusa excusa) {
        if (puedeManejar(excusa)) {
            procesar(excusa);
        } else if (siguiente != null) {
            siguiente.manejarExcusa(excusa);
        } else {
            throw new ExcusaNoManejadaException("Excusa no manejada: " + excusa.getMotivo().getClass().getSimpleName());
        }
    }
}

