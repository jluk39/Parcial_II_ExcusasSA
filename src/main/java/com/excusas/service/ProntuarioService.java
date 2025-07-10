// src/main/java/com/excusas/service/ProntuarioService.java
package com.excusas.service;

import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.excusa.IExcusa;
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

    private ExcusaResponseDTO convertirAProntuarioDTO(IExcusa excusa) {
        ExcusaResponseDTO dto = new ExcusaResponseDTO();
        dto.setId(excusa.getId());
        dto.setEmpleadoNombre(excusa.getEmpleado().getNombre());
        dto.setTipoMotivo(determinarTipoMotivo(excusa));
        dto.setDescripcion(excusa.getDescripcion());
        dto.setEstado(excusa.getEstado());
        dto.setFechaCreacion(LocalDateTime.now());
        dto.setEncargadoProcesador(excusa.getEncargadoProcesador());
        return dto;
    }

    private String determinarTipoMotivo(IExcusa excusa) {
        return excusa.getMotivo().getTipoMotivo();
    }
}