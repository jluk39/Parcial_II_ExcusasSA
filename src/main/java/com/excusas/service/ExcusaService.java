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

    public ExcusaResponseDTO crearExcusa(ExcusaRequestDTO request) {
        try {
            // Validaciones de negocio
            validarRequest(request);

            // Crear o buscar empleado
            Empleado empleado = obtenerOCrearEmpleado(request);

            // Crear motivo validado
            IMotivoExcusa motivo = motivoService.crearMotivo(request.getTipoMotivo());

            // Generar excusa
            IExcusa iExcusa = empleado.generarExcusa(motivo);
            Excusa excusa = (Excusa) iExcusa; // Cast necesario para JPA

            // Procesar por cadena de responsabilidad
            IEncargado cadenaEncargados = LineaDeEncargados.crearCadena();
            cadenaEncargados.manejarExcusa(excusa);

            // Persistir
            excusa = excusaRepository.save(excusa);

            return convertirAResponseDTO(excusa, request.getDescripcion());
        } catch (Exception e) {
            throw new RuntimeException("Error al crear excusa: " + e.getMessage());
        }
    }

    private void validarRequest(ExcusaRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser null");
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }

        if (request.getDescripcion().length() > 500) {
            throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
        }
    }

    private Empleado obtenerOCrearEmpleado(ExcusaRequestDTO request) {
        // Buscar empleado existente por legajo
        return empleadoService.buscarPorLegajo(request.getEmpleadoLegajo())
                .orElseGet(() -> empleadoService.crearEmpleado(
                        request.getEmpleadoNombre(),
                        request.getEmpleadoEmail(),
                        request.getEmpleadoLegajo()
                ));
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
            throw new RuntimeException("Error en formato de fechas. Use formato: YYYY-MM-DD");
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
            throw new RuntimeException("Error en formato de fecha. Use formato: YYYY-MM-DD");
        }
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
}