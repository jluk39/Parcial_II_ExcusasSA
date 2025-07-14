package com.excusas.service;

public interface IEmailSender {
    void enviarEmail(String destino, String origen, String asunto, String cuerpo);
}
