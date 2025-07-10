// src/main/java/com/excusas/model/excusa/IExcusa.java
package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.motivo.IMotivoExcusa;

public interface IExcusa {
    IMotivoExcusa getMotivo();
    Empleado getEmpleado();
    Long getId();
    void setId(Long id);

    default String getDescripcion() {
        return getMotivo().getClass().getSimpleName();
    }
    void setEstado(String estado);
    void setEncargadoProcesador(String encargado);

    default String getEstado() {
        return "PENDIENTE";
    }

    default String getEncargadoProcesador() {
        return "Sistema";
    }
}