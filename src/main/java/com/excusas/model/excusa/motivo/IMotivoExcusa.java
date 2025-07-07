package com.excusas.model.excusa.motivo;

import com.excusas.model.empleado.encargado.*;
import com.excusas.model.excusa.IExcusa;

public interface IMotivoExcusa {
    boolean esTrivial();
    boolean esModerada();
    boolean esCompleja();
    boolean esInverosimil();
    void procesarPor(Recepcionista r, IExcusa excusa);
    void procesarPor(SupervisorArea s, IExcusa excusa);
    void procesarPor(GerenteRRHH g, IExcusa excusa);
    void procesarPor(CEO c, IExcusa excusa);
}
