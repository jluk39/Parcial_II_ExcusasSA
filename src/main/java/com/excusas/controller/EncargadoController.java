// src/main/java/com/excusas/controller/EncargadoController.java
package com.excusas.controller;

import com.excusas.dto.EncargadoStatusDTO;
import com.excusas.dto.CambiarModoRequestDTO;
import com.excusas.service.EncargadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encargados")
public class EncargadoController {

    @Autowired
    private EncargadoService encargadoService;

    @GetMapping
    public ResponseEntity<List<EncargadoStatusDTO>> obtenerEstadoEncargados() {
        List<EncargadoStatusDTO> encargados = encargadoService.obtenerEstadoCadena();
        return ResponseEntity.ok(encargados);
    }

    @PostMapping("/cambiarModo")
    public ResponseEntity<String> cambiarModoResolucion(@RequestBody CambiarModoRequestDTO request) {
        try {
            encargadoService.cambiarModo(request.getEncargadoId(), request.getNuevoModo());
            return ResponseEntity.ok("Modo cambiado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}