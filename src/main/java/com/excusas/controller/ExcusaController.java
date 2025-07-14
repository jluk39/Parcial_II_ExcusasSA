// src/main/java/com/excusas/controller/ExcusaController.java
package com.excusas.controller;

import com.excusas.dto.ExcusaRequestDTO;
import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.service.ExcusaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/excusas")
@CrossOrigin(origins = "*")
public class ExcusaController {

    @Autowired
    private ExcusaService excusaService;

    @PostMapping
    public ResponseEntity<ExcusaResponseDTO> crearExcusa(@RequestBody ExcusaRequestDTO request) {
        try {
            ExcusaResponseDTO response = excusaService.crearExcusa(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerTodasExcusas() {
        List<ExcusaResponseDTO> excusas = excusaService.obtenerTodasExcusas();
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExcusaResponseDTO> obtenerExcusaPorId(@PathVariable Long id) {
        ExcusaResponseDTO excusa = excusaService.obtenerExcusaPorId(id);
        if (excusa != null) {
            return ResponseEntity.ok(excusa);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/empleado/{nombre}")
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerExcusasPorEmpleado(@PathVariable String nombre) {
        List<ExcusaResponseDTO> excusas = excusaService.obtenerExcusasPorEmpleado(nombre);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/legajo/{legajo}")
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerExcusasPorLegajo(@PathVariable Integer legajo) {
        List<ExcusaResponseDTO> excusas = excusaService.obtenerExcusasPorLegajo(legajo);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/busqueda")
    public ResponseEntity<List<ExcusaResponseDTO>> buscarExcusas(
            @RequestParam Integer legajo,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta) {
        List<ExcusaResponseDTO> excusas = excusaService.buscarExcusas(legajo, fechaDesde, fechaHasta);
        return ResponseEntity.ok(excusas);
    }

    @GetMapping("/rechazadas")
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerExcusasRechazadas() {
        List<ExcusaResponseDTO> excusas = excusaService.obtenerExcusasRechazadas();
        return ResponseEntity.ok(excusas);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminarExcusasPorFecha(@RequestParam(required = false) String fechaLimite) {
        if (fechaLimite == null || fechaLimite.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El parámetro fechaLimite es obligatorio");
        }

        try {
            int excusasEliminadas = excusaService.eliminarExcusasAnterioresA(fechaLimite);
            return ResponseEntity.ok("Se eliminaron " + excusasEliminadas + " excusas");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}