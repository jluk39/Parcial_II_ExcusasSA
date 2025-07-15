package com.excusas.service;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.model.empleado.Empleado;
import com.excusas.model.empleado.encargado.IEncargado;
import com.excusas.model.excusa.IExcusa;
import com.excusas.model.excusa.Excusa;
import com.excusas.model.excusa.motivo.IMotivoExcusa;
import com.excusas.model.excusa.motivo.Trivial;
import com.excusas.model.excusa.motivo.Moderada;
import com.excusas.model.excusa.motivo.Compleja;
import com.excusas.repository.ExcusaRepository;
import com.excusas.excepciones.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcusaService {

    @Autowired
    private ExcusaRepository excusaRepository;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private MotivoService motivoService;

    private void validarRequest(ExcusaRequestDTO request) {
        if (request == null) {
            throw new BusinessException("El request no puede ser null");
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            throw new BusinessException("La descripción es obligatoria");
        }

        if (request.getDescripcion().length() > 500) {
            throw new BusinessException("La descripción no puede exceder 500 caracteres");
        }
    }

    private Empleado obtenerOCrearEmpleado(ExcusaRequestDTO request) {
        if (request.getEmpleadoLegajo() == null || request.getEmpleadoLegajo() <= 0) {
            throw new BusinessException("El legajo del empleado es obligatorio y debe ser positivo");
        }
        if (request.getEmpleadoNombre() == null || request.getEmpleadoNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre del empleado es obligatorio");
        }
        if (request.getEmpleadoEmail() == null || !request.getEmpleadoEmail().contains("@")) {
            throw new BusinessException("El email del empleado debe ser válido");
        }
        return empleadoService.buscarPorLegajo(request.getEmpleadoLegajo())
                .orElseGet(() -> empleadoService.crearEmpleado(
                        request.getEmpleadoNombre(),
                        request.getEmpleadoEmail(),
                        request.getEmpleadoLegajo()
                ));
    }

    public ExcusaResponseDTO crearExcusa(ExcusaRequestDTO request) {
        try {
            validarRequest(request);
            if (request.getTipoMotivo() == null || request.getTipoMotivo().trim().isEmpty()) {
                throw new BusinessException("El tipo de motivo es obligatorio");
            }
            IMotivoExcusa motivo;
            try {
                motivo = motivoService.crearMotivo(request.getTipoMotivo());
            } catch (Exception e) {
                throw new BusinessException("Tipo de motivo no válido: " + request.getTipoMotivo());
            }
            Empleado empleado = obtenerOCrearEmpleado(request);
            IExcusa iExcusa = empleado.generarExcusa(motivo);
            Excusa excusa = (Excusa) iExcusa;
            IEncargado cadenaEncargados = LineaDeEncargados.crearCadena();
            cadenaEncargados.manejarExcusa(excusa);
            excusa = excusaRepository.save(excusa);
            return convertirAResponseDTO(excusa, request.getDescripcion());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error al crear excusa: " + e.getMessage());
        }
    }

    private IMotivoExcusa crearMotivo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "trivial" -> new Trivial();
            case "moderada" -> new Moderada() {};
            case "compleja" -> new Compleja();
            default -> throw new BusinessException("Tipo de motivo no válido: " + tipo);
        };
    }

    // Sobrecarga 1: para crear (con descripción del request)
    private ExcusaResponseDTO convertirAResponseDTO(IExcusa excusa, String descripcion) {
        ExcusaResponseDTO response = new ExcusaResponseDTO();
        response.setId(excusa.getId());
        response.setEmpleadoNombre(excusa.getEmpleado() != null ? excusa.getEmpleado().getNombre() : "Sin empleado");
        response.setTipoMotivo(excusa.getMotivo() != null ? determinarTipoMotivo(excusa.getMotivo()) : "Sin motivo");
        response.setDescripcion(descripcion != null ? descripcion : "Sin descripción");
        response.setEstado(excusa.getEstado() != null ? excusa.getEstado() : "PENDIENTE");
        response.setFechaCreacion(LocalDateTime.now());
        response.setEncargadoProcesador(excusa.getEncargadoProcesador() != null ? excusa.getEncargadoProcesador() : "Sistema");
        return response;
    }

    // Sobrecarga 2: para obtener (sin descripción personalizada)
    private ExcusaResponseDTO convertirAResponseDTO(IExcusa excusa) {
        return convertirAResponseDTO(excusa, excusa.getDescripcion());
    }

    private String determinarTipoMotivo(IMotivoExcusa motivo) {
        if (motivo == null) return "Desconocido";
        try {
            return motivo.getTipoMotivo();
        } catch (Exception e) {
            return motivo.getClass().getSimpleName();
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

    public List<ExcusaResponseDTO> obtenerExcusasPorLegajo(Integer legajo) {
        return excusaRepository.findByEmpleadoLegajo(legajo).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ExcusaResponseDTO> buscarExcusas(Integer legajo, String fechaDesde, String fechaHasta) {
        try {
            LocalDateTime desde = fechaDesde != null ? LocalDateTime.parse(fechaDesde + "T00:00:00") : null;
            LocalDateTime hasta = fechaHasta != null ? LocalDateTime.parse(fechaHasta + "T23:59:59") : null;

            if (desde != null && hasta != null) {
                return excusaRepository.findByEmpleadoLegajoAndFechaCreacionBetween(legajo, desde, hasta).stream()
                        .map(this::convertirAResponseDTO)
                        .collect(Collectors.toList());
            } else {
                return obtenerExcusasPorLegajo(legajo);
            }
        } catch (Exception e) {
            throw new BusinessException("Error en formato de fechas. Use formato: YYYY-MM-DD");
        }
    }

    public List<ExcusaResponseDTO> obtenerExcusasRechazadas() {
        return excusaRepository.findByEstado("RECHAZADA").stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public int eliminarExcusasAnterioresA(String fechaLimite) {
        try {
            LocalDateTime limite = LocalDateTime.parse(fechaLimite + "T23:59:59");
            return excusaRepository.deleteByFechaCreacionBefore(limite);
        } catch (Exception e) {
            throw new BusinessException("Error en formato de fecha. Use formato: YYYY-MM-DD");
        }
    }
}