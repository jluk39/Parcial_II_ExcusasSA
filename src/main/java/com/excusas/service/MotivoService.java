// src/main/java/com/excusas/service/MotivoService.java
package com.excusas.service;

import com.excusas.model.excusa.motivo.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MotivoService {

    public IMotivoExcusa crearMotivo(String tipo) {
        validarTipoMotivo(tipo);

        return switch (tipo.toLowerCase()) {
            case "trivial" -> new Trivial();
            case "moderada" -> new Moderada() {}; // Implementación anónima
            case "compleja" -> new Compleja();
            case "inverosimil" -> new Inverosimil();
            case "corteluz" -> new CorteLuz();
            case "cuidadofamiliar" -> new CuidadoFamiliar();
            default -> throw new IllegalArgumentException("Tipo de motivo no válido: " + tipo);
        };
    }

    public List<String> obtenerTiposValidos() {
        return Arrays.asList("trivial", "moderada", "compleja", "inverosimil", "corteluz", "cuidadofamiliar");
    }

    public boolean esTipoValido(String tipo) {
        return obtenerTiposValidos().contains(tipo.toLowerCase());
    }

    private void validarTipoMotivo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de motivo es obligatorio");
        }

        if (!esTipoValido(tipo)) {
            throw new IllegalArgumentException(
                "Tipo de motivo no válido. Tipos válidos: " + String.join(", ", obtenerTiposValidos())
            );
        }
    }
}