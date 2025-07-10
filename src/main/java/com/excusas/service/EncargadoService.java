// src/main/java/com/excusas/service/EncargadoService.java
package com.excusas.service;

import com.excusas.dto.EncargadoStatusDTO;
import com.excusas.model.empleado.encargado.IEncargado;
import com.excusas.model.empleado.encargado.EncargadoBase;
import com.excusas.model.estrategia.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class EncargadoService {

    private IEncargado cadenaEncargados;
    private Map<String, EncargadoBase> encargadosMap;

    public EncargadoService() {
        inicializarCadena();
    }

    private void inicializarCadena() {
        this.cadenaEncargados = LineaDeEncargados.crearCadena();
        this.encargadosMap = new HashMap<>();

        // Mapear encargados para poder encontrarlos por ID
        IEncargado actual = cadenaEncargados;
        while (actual != null && actual instanceof EncargadoBase) {
            EncargadoBase encargado = (EncargadoBase) actual;
            encargadosMap.put(encargado.getNombre().toLowerCase(), encargado);
            actual = encargado.getSiguiente();
        }
    }

    public List<EncargadoStatusDTO> obtenerEstadoCadena() {
        List<EncargadoStatusDTO> estadoEncargados = new ArrayList<>();

        IEncargado actual = cadenaEncargados;
        while (actual != null && actual instanceof EncargadoBase) {
            EncargadoBase encargado = (EncargadoBase) actual;

            EncargadoStatusDTO status = new EncargadoStatusDTO(
                encargado.getNombre(),
                determinarCargo(encargado),
                determinarModoActual(encargado),
                true // Asumimos que todos están activos
            );

            estadoEncargados.add(status);
            actual = encargado.getSiguiente();
        }

        return estadoEncargados;
    }

    public void cambiarModo(String encargadoId, String nuevoModo) {
        EncargadoBase encargado = encargadosMap.get(encargadoId.toLowerCase());

        if (encargado == null) {
            throw new IllegalArgumentException("Encargado no encontrado: " + encargadoId);
        }

        IModoResolucion modo = crearModo(nuevoModo);
        encargado.setModo(modo);
        System.out.println("Modo de " + encargado.getNombre() + " cambiado a: " + nuevoModo);
    }

    private String determinarCargo(EncargadoBase encargado) {
        String className = encargado.getClass().getSimpleName();
        return switch (className) {
            case "Recepcionista" -> "Recepcionista";
            case "SupervisorArea" -> "Supervisor de Área";
            case "GerenteRRHH" -> "Gerente de RRHH";
            case "CEO" -> "CEO";
            default -> "Encargado";
        };
    }

    private String determinarModoActual(EncargadoBase encargado) {
        // Por ahora retornamos "Normal", pero podrías agregar un getter al EncargadoBase
        return "Normal";
    }

    private IModoResolucion crearModo(String tipoModo) {
        return switch (tipoModo.toLowerCase()) {
            case "normal" -> new Normal();
            case "vago" -> new Vago();
            case "productivo" -> new Productivo();
            default -> throw new IllegalArgumentException("Modo no válido: " + tipoModo);
        };
    }
}