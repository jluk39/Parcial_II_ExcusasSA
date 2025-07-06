package com.excusas.excusa;

import com.excusas.empleado.Empleado;
import com.excusas.excusa.motivo.IMotivoExcusa;

public interface IExcusa {
    IMotivoExcusa getMotivo();
    Empleado getEmpleado();
}
