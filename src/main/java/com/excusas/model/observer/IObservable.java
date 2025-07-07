package com.excusas.model.observer;

public interface IObservable {
    void agregarObserver(IObserver observer);
    void quitarObserver(IObserver observer);
    void notificarObservers();
}
