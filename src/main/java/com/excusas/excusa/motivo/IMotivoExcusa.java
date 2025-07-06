package com.excusas.excusa.motivo;

import com.excusas.excusa.IExcusa;
import com.excusas.empleado.encargado.*;

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
