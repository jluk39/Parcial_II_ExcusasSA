package com.excusas;

import com.excusas.empleado.Empleado;
import com.excusas.excusa.IExcusa;
import com.excusas.excusa.motivo.Trivial;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmpleadoExcusasTest {

    @Test
    public void testGenerarExcusaTrivial() {
        Empleado empleado = new Empleado("Lucía", "lucia@mail.com", 123);
        IExcusa excusa = empleado.generarExcusa(new Trivial());

        assertNotNull(excusa);
        assertEquals("Trivial", excusa.getMotivo().getClass().getSimpleName());
        assertEquals("Lucía", excusa.getEmpleado().getNombre());
    }
}
