package com.excusas;

import com.excusas.empleado.Empleado;
import com.excusas.empleado.encargado.IEncargado;
import com.excusas.empleado.encargado.Recepcionista;
import com.excusas.estrategia.Normal;
import com.excusas.estrategia.Vago;
import com.excusas.estrategia.Productivo;
import com.excusas.excusa.Excusa;
import com.excusas.excusa.IExcusa;
import com.excusas.excusa.motivo.Inverosimil;
import com.excusas.excusa.motivo.Trivial;
import com.excusas.servicio.IEmailSender;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModoResolucionTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void redirectOutput() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    @Test
    void testModoNormalProcesaExcusaTrivial() {
        Empleado empleado = new Empleado("Jere", "jerito01@mail.com", 1);
        IExcusa excusa = empleado.generarExcusa(new Trivial());

        IEmailSender dummy = (to, from, subject, body) -> {};
        IEncargado encargado = new Recepcionista("Lucia", "luciadiaz@mail.com", 10, new Normal(), dummy);
        encargado.manejarExcusa(excusa);

        String output = outContent.toString().toLowerCase();
        assertTrue(output.contains("recepcionista procesó la excusa"));
    }

    @Test
    void testModoVagoNoHaceNada() {
        Empleado empleado = new Empleado("Jere", "jerito01@mail.com", 1);
        IExcusa excusa = empleado.generarExcusa(new Trivial());

        IEmailSender dummy = (to, from, subject, body) -> {};
        IEncargado encargado = new Recepcionista("Lucia", "luciadiaz@mail.com", 10, new Vago(), dummy);
        encargado.manejarExcusa(excusa);

        String output = outContent.toString().toLowerCase();
        assertTrue(output.contains("está ocupado"));
    }

    @Test
    void testModoProductivoProcesaSiPuede() {
        Empleado empleado = new Empleado("Jere", "jerito01@mail.com", 1);
        IExcusa excusa = empleado.generarExcusa(new Trivial());

        IEmailSender dummy = (to, from, subject, body) -> {};
        IEncargado encargado = new Recepcionista("Lucia", "luciadiaz@mail.com", 10, new Productivo(), dummy);
        encargado.manejarExcusa(excusa);

        String output = outContent.toString().toLowerCase();
        assertTrue(output.contains("recepcionista procesó la excusa"));
    }

    @Test
    void testModoProductivoInformaSiNoPuede() {
        Empleado empleado = new Empleado("Jere", "jerito01@mail.com", 1);
        IExcusa excusa = empleado.generarExcusa(new Inverosimil());

        IEmailSender dummy = (to, from, subject, body) -> {};
        IEncargado encargado = new Recepcionista("Jere", "jerito01@mail.com", 10, new Productivo(), dummy);
        encargado.manejarExcusa(excusa);

        String output = outContent.toString().toLowerCase();
        assertTrue(output.contains("no puede resolver esto, delegando"));
    }
}
