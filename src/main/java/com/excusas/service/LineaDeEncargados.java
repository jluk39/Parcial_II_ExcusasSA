package com.excusas.service;

import com.excusas.model.estrategia.Normal;
import com.excusas.model.empleado.encargado.*;

public class LineaDeEncargados {

    public static IEncargado crearCadena() {
        EmailSenderImpl emailSender = new EmailSenderImpl();

        IEncargado rechazador = new Rechazador(emailSender);
        CEO ceo = new CEO("Romina", "laRomi@mail.com", 4, new Normal(), emailSender);
        GerenteRRHH gerente = new GerenteRRHH("Roberto", "robertito05@mail.com", 3, new Normal(), emailSender);
        SupervisorArea supervisor = new SupervisorArea("Luis", "luisito@mail.com", 2, new Normal(), emailSender);
        Recepcionista recepcionista = new Recepcionista("Jeremias", "jeremiadiaz@mail.com", 1, new Normal(), emailSender);

        recepcionista.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(ceo);
        ceo.setSiguiente(rechazador);

        return recepcionista; // punto de entrada
    }
}
