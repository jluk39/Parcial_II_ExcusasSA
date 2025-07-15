// src/main/java/com/excusas/controller/ProntuarioController.java
package com.excusas.controller;

import com.excusas.dto.ExcusaResponseDTO;
import com.excusas.service.ProntuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService prontuarioService;

    @GetMapping
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerProntuarios() {
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerTodosProntuarios();
        return ResponseEntity.ok(prontuarios);
    }

    @GetMapping("/empleado/{nombre}")
    public ResponseEntity<List<ExcusaResponseDTO>> obtenerProntuarioPorEmpleado(@PathVariable String nombre) {
        List<ExcusaResponseDTO> prontuarios = prontuarioService.obtenerProntuarioPorEmpleado(nombre);
        return ResponseEntity.ok(prontuarios);
    }
}
