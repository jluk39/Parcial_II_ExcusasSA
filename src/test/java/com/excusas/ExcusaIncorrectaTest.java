package com.excusas;

import com.excusas.model.empleado.Empleado;
import com.excusas.model.empleado.encargado.Recepcionista;
import com.excusas.model.estrategia.Normal;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.Inverosimil;
import com.excusas.service.EmailSenderImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExcusaIncorrectaTest {

    @Test
    public void testRecepcionistaNoProcesaExcusaInverosimil() {
        Recepcionista recepcionista = new Recepcionista("Jeremias", "jere@mail.com", 1, new Normal(), new EmailSenderImpl());
        Empleado empleado = new Empleado("Lucía", "lucia@mail.com", 102);
        IExcusa excusa = empleado.generarExcusa(new Inverosimil());

        assertFalse(recepcionista.puedeManejar(excusa));
    }
}
