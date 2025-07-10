// src/main/java/com/excusas/service/ValidacionService.java
package com.excusas.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidacionService {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public boolean esEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean esNombreValido(String nombre) {
        return nombre != null &&
               nombre.trim().length() >= 2 &&
               nombre.trim().length() <= 100 &&
               nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public boolean esLegajoValido(Integer legajo) {
        return legajo != null && legajo > 0 && legajo <= 999999;
    }

    public boolean esDescripcionValida(String descripcion) {
        return descripcion != null &&
               descripcion.trim().length() >= 10 &&
               descripcion.trim().length() <= 500;
    }
}