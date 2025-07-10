// src/main/java/com/excusas/service/EmpleadoService.java
package com.excusas.service;

import com.excusas.model.empleado.Empleado;
import com.excusas.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado crearEmpleado(String nombre, String email, Integer legajo) {
        // Validaciones de negocio
        validarDatosEmpleado(nombre, email, legajo);

        // Verificar que no existe empleado con mismo legajo
        if (empleadoRepository.findByLegajo(legajo).isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con legajo: " + legajo);
        }

        Empleado empleado = new Empleado(nombre, email, legajo);
        return empleadoRepository.save(empleado);
    }

    public List<Empleado> obtenerTodosEmpleados() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> buscarPorLegajo(Integer legajo) {
        return empleadoRepository.findByLegajo(legajo);
    }

    public Optional<Empleado> buscarPorNombre(String nombre) {
        return empleadoRepository.findByNombre(nombre);
    }

    private void validarDatosEmpleado(String nombre, String email, Integer legajo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email debe ser válido");
        }

        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }
    }
}