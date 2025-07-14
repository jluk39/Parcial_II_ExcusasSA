// src/main/java/com/excusas/service/ProntuarioService.java
package com.excusas.service;

import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.excusa.Excusa;
import com.excusas.repository.ExcusaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProntuarioService {

    @Autowired
    private ExcusaRepository excusaRepository;

    public List<ExcusaResponseDTO> obtenerTodosProntuarios() {
        return excusaRepository.findAll().stream()
                .filter(excusa -> "PROCESADA".equals(excusa.getEstado()) ||
                                "RECHAZADA".equals(excusa.getEstado()))
                .map(this::convertirAProntuarioDTO)
                .collect(Collectors.toList());
    }

    public List<ExcusaResponseDTO> obtenerProntuarioPorEmpleado(String nombreEmpleado) {
        return excusaRepository.findByEmpleadoNombre(nombreEmpleado).stream()
                .filter(excusa -> "PROCESADA".equals(excusa.getEstado()) ||
                                "RECHAZADA".equals(excusa.getEstado()))
                .map(this::convertirAProntuarioDTO)
                .collect(Collectors.toList());
    }

    private ExcusaResponseDTO convertirAProntuarioDTO(Excusa excusa) {
        ExcusaResponseDTO dto = new ExcusaResponseDTO();
        dto.setId(excusa.getId());
        dto.setEmpleadoNombre(excusa.getEmpleado() != null ? excusa.getEmpleado().getNombre() : "Sin empleado");
        dto.setTipoMotivo(excusa.getTipoMotivo() != null ? excusa.getTipoMotivo() : "Sin motivo");
        dto.setDescripcion(excusa.getDescripcion() != null ? excusa.getDescripcion() : "Sin descripción");
        dto.setEstado(excusa.getEstado());
        dto.setFechaCreacion(excusa.getFechaCreacion() != null ? excusa.getFechaCreacion() : LocalDateTime.now());
        dto.setEncargadoProcesador(excusa.getEncargadoProcesador() != null ? excusa.getEncargadoProcesador() : "Sistema");
        return dto;
    }
}