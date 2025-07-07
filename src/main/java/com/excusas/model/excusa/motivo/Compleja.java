package com.excusas.model.excusa.motivo;

public class Compleja extends MotivoExcusa {
    @Override
    public boolean esTrivial() {
        return false;
    }

    @Override
    public boolean esModerada() {
        return false;
    }

    @Override
    public boolean esCompleja() {
        return true;
    }

    @Override
    public boolean esInverosimil() {
        return false;
    }
}
