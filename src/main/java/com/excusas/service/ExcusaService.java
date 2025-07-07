package com.excusas.service;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.empleado.Empleado;
import com.excusas.model.empleado.encargado.IEncargado;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.motivo.IMotivoExcusa;
import com.excusas.model.excusa.motivo.Trivial;
import com.excusas.model.excusa.motivo.Moderada;
import com.excusas.model.excusa.motivo.Compleja;
import com.excusas.repository.ExcusaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcusaService {

    @Autowired
    private ExcusaRepository excusaRepository;

    public ExcusaResponseDTO crearExcusa(ExcusaRequestDTO request) {
        try {
            Empleado empleado = new Empleado(
                    request.getEmpleadoNombre(),
                    request.getEmpleadoEmail(),
                    request.getEmpleadoLegajo()
            );

            IMotivoExcusa motivo = crearMotivo(request.getTipoMotivo());
            IExcusa excusa = empleado.generarExcusa(motivo);

            IEncargado cadenaEncargados = LineaDeEncargados.crearCadena();
            cadenaEncargados.procesar(excusa);

            excusa = excusaRepository.save(excusa);

            return convertirAResponseDTO(excusa, request.getDescripcion());
        } catch (Exception e) {
            throw new RuntimeException("Error al crear excusa: " + e.getMessage());
        }
    }

    public List<ExcusaResponseDTO> obtenerTodasExcusas() {
        return excusaRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public ExcusaResponseDTO obtenerExcusaPorId(Long id) {
        return excusaRepository.findById(id)
                .map(this::convertirAResponseDTO)
                .orElse(null);
    }

    public List<ExcusaResponseDTO> obtenerExcusasPorEmpleado(String nombreEmpleado) {
        return excusaRepository.findByEmpleadoNombre(nombreEmpleado).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    private IMotivoExcusa crearMotivo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "trivial" -> new Trivial();
            case "moderada" -> new Moderada() {};
            case "compleja" -> new Compleja();
            default -> throw new IllegalArgumentException("Tipo de motivo no válido: " + tipo);
        };
    }

    // Sobrecarga 1: para crear (con descripción del request)
    private ExcusaResponseDTO convertirAResponseDTO(IExcusa excusa, String descripcion) {
        ExcusaResponseDTO response = new ExcusaResponseDTO();
        response.setId(excusa.getId());
        response.setEmpleadoNombre(excusa.getEmpleado().getNombre());
        response.setTipoMotivo(determinarTipoMotivo(excusa.getMotivo()));
        response.setDescripcion(descripcion);
        response.setEstado(excusa.getEstado());
        response.setFechaCreacion(LocalDateTime.now());
        response.setEncargadoProcesador(excusa.getEncargadoProcesador());
        return response;
    }

    // Sobrecarga 2: para obtener (sin descripción personalizada)
    private ExcusaResponseDTO convertirAResponseDTO(IExcusa excusa) {
        return convertirAResponseDTO(excusa, "Descripción no disponible");
    }

    private String determinarTipoMotivo(IMotivoExcusa motivo) {
        if (motivo.esTrivial()) return "Trivial";
        if (motivo.esModerada()) return "Moderada";
        if (motivo.esCompleja()) return "Compleja";
        return "Desconocido";
    }
}