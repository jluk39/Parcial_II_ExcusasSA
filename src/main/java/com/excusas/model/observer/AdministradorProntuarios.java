package com.excusas.model.observer;

import com.excusas.model.excusa.Prontuario;
import java.util.ArrayList;
import java.util.List;

public class AdministradorProntuarios implements IObservable {

    private static AdministradorProntuarios instancia;
    private final List<IObserver> observers = new ArrayList<>();
    private final List<Prontuario> prontuarios = new ArrayList<>();
    private Prontuario ultimoProntuario;

    private AdministradorProntuarios() {}

    public static AdministradorProntuarios getInstancia() {
        if (instancia == null) {
            instancia = new AdministradorProntuarios();
        }
        return instancia;
    }

    public void guardarProntuario(Prontuario prontuario) {
        prontuarios.add(prontuario);
        this.ultimoProntuario = prontuario;
        notificarObservers();
    }

    public Prontuario getUltimoProntuario() {
        return ultimoProntuario;
    }

    @Override
    public void agregarObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void quitarObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers() {
        for (IObserver observer : observers) {
            observer.actualizar(ultimoProntuario);
        }
    }
}

