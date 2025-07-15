package com.excusas.controller;

import com.excusas.dto.EmpleadoRequestDTO;
import com.excusas.dto.EmpleadoResponseDTO;
import com.excusas.model.empleado.Empleado;
import com.excusas.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerTodosEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodosEmpleados();
        List<EmpleadoResponseDTO> response = empleados.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@RequestBody EmpleadoRequestDTO request) {
        try {
            Empleado empleado = empleadoService.crearEmpleado(
                    request.getNombre(),
                    request.getEmail(),
                    request.getLegajo()
            );
            return ResponseEntity.ok(convertirAResponseDTO(empleado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoResponseDTO> obtenerEmpleadoPorLegajo(@PathVariable Integer legajo) {
        return empleadoService.buscarPorLegajo(legajo)
                .map(empleado -> ResponseEntity.ok(convertirAResponseDTO(empleado)))
                .orElse(ResponseEntity.notFound().build());
    }

    private EmpleadoResponseDTO convertirAResponseDTO(Empleado empleado) {
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setEmail(empleado.getEmail());
        dto.setLegajo(empleado.getLegajo());
        return dto;
    }
}
