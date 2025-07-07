// src/main/java/com/excusas/model/excusa/IExcusa.java
package com.excusas.model.excusa;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.excusa.motivo.IMotivoExcusa;

public interface IExcusa {
    IMotivoExcusa getMotivo();
    Empleado getEmpleado();
    Long getId();
    void setId(Long id);

    // Agregar estos métodos opcionales
    default String getDescripcion() {
        return getMotivo().getClass().getSimpleName();
    }

    default String getEstado() {
        return "PROCESADA";
    }

    default String getEncargadoProcesador() {
        return "Sistema";
    }
}