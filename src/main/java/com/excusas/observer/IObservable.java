package com.excusas.observer;

public interface IObservable {
    void agregarObserver(IObserver observer);
    void quitarObserver(IObserver observer);
    void notificarObservers();
}
