package com.excusas.service;

public class EmailSenderImpl implements IEmailSender {

    @Override
    public void enviarEmail(String destino, String origen, String asunto, String cuerpo) {
        System.out.println("------ Email Enviado ------");
        System.out.println("De: " + origen);
        System.out.println("Para: " + destino);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + cuerpo);
        System.out.println("----------------------------");
    }
}
