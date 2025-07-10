package com.excusas.model.empleado.encargado;

import com.excusas.model.excusa.IExcusa;
import com.excusas.model.estrategia.IModoResolucion;
import com.excusas.service.IEmailSender;

public abstract class EncargadoBase implements IEncargado {
    private final String nombre;
    private final String email;
    private final int legajo;
    private IModoResolucion modo;
    private final IEmailSender emailSender;
    private IEncargado siguiente;

    public EncargadoBase(String nombre, String email, int legajo,
                        IModoResolucion modo, IEmailSender emailSender) {
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
        this.modo = modo;
        this.emailSender = emailSender;
    }

    @Override
    public final void manejarExcusa(IExcusa excusa) {
        if (puedeManejar(excusa)) {
            modo.resolver(this, excusa);
        } else if (siguiente != null) {
            siguiente.manejarExcusa(excusa);
        } else {
            System.out.println("Excusa rechazada: ningún encargado puede procesarla");
            excusa.setEstado("RECHAZADA");
        }
    }

    public void manejarPorDefecto(IExcusa excusa) {
        procesar(excusa);
        excusa.setEstado("PROCESADA");
        excusa.setEncargadoProcesador(this.getClass().getSimpleName());
        System.out.println(this.getClass().getSimpleName() + " procesó la excusa");
    }

    // Métodos abstractos que deben implementar las subclases
    public abstract boolean puedeManejar(IExcusa excusa);
    public abstract void procesar(IExcusa excusa);

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public int getLegajo() {
        return legajo;
    }

    public IModoResolucion getModo() {
        return modo;
    }

    public void setModo(IModoResolucion modo) {
        this.modo = modo;
    }

    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public IEncargado getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(IEncargado siguiente) {
        this.siguiente = siguiente;
    }
}